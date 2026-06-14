package io.github.sophon.quexplorer.feat.scanner.native

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.AVFoundation.AVCaptureConnection
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureMetadataOutput
import platform.AVFoundation.AVCaptureMetadataOutputObjectsDelegateProtocol
import platform.AVFoundation.AVCaptureOutput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.AVMetadataMachineReadableCodeObject
import platform.AVFoundation.AVMetadataObjectTypeQRCode
import platform.CoreGraphics.CGRectZero
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIView
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue
import platform.darwin.dispatch_get_main_queue
import platform.posix.QOS_CLASS_USER_INITIATED

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Composable
internal actual fun CameraPreview(
    onDetectQr: (data: String) -> Unit,
    modifier: Modifier,
) {
    val session = remember { AVCaptureSession() }
    val previewLayer = remember { AVCaptureVideoPreviewLayer(session = session) }
    val delegate = remember { QrMetadataDelegate(onDetectQr) }

    SideEffect {
        delegate.onDetect = onDetectQr
    }

    DisposableEffect(Unit) {
        val queue = dispatch_get_global_queue(QOS_CLASS_USER_INITIATED.toLong(), 0UL)
        dispatch_async(queue) {
            configureSession(session, delegate)
            session.startRunning()
        }
        onDispose {
            dispatch_async(queue) {
                session.stopRunning()
            }
        }
    }

    UIKitView(
        modifier = modifier,
        factory = {
            return@UIKitView CameraPreviewView(previewLayer)
        },
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun configureSession(
    session: AVCaptureSession,
    delegate: AVCaptureMetadataOutputObjectsDelegateProtocol,
) {
    session.beginConfiguration()

    val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
    if (device == null) {
        session.commitConfiguration()
        return
    }

    val input = AVCaptureDeviceInput.deviceInputWithDevice(device, null)
    if (input == null) {
        session.commitConfiguration()
        return
    }
    if (session.canAddInput(input)) {
        session.addInput(input)
    }

    val output = AVCaptureMetadataOutput()
    if (session.canAddOutput(output)) {
        session.addOutput(output)
        output.setMetadataObjectsDelegate(delegate, dispatch_get_main_queue())
        // metadataObjectTypes must be set AFTER addOutput — availableMetadataObjectTypes
        // is empty until the output is wired into the session.
        val available = output.availableMetadataObjectTypes.orEmpty()
        if (available.contains(AVMetadataObjectTypeQRCode)) {
            output.metadataObjectTypes = listOf(AVMetadataObjectTypeQRCode)
        }
    }

    session.commitConfiguration()
}

@OptIn(BetaInteropApi::class)
private class QrMetadataDelegate(
    var onDetect: (String) -> Unit,
) : NSObject(), AVCaptureMetadataOutputObjectsDelegateProtocol {
    override fun captureOutput(
        output: AVCaptureOutput,
        didOutputMetadataObjects: List<*>,
        fromConnection: AVCaptureConnection,
    ) {
        val code = didOutputMetadataObjects.firstOrNull() as? AVMetadataMachineReadableCodeObject ?: return
        val value = code.stringValue ?: return
        onDetect(value)
    }
}

@OptIn(ExperimentalForeignApi::class)
private class CameraPreviewView(
    private val previewLayer: AVCaptureVideoPreviewLayer,
) : UIView(frame = CGRectZero.readValue()) {

    init {
        previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill
        layer.addSublayer(previewLayer)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        CATransaction.begin()
        CATransaction.setValue(true, kCATransactionDisableActions)
        previewLayer.setFrame(bounds)
        CATransaction.commit()
    }
}
