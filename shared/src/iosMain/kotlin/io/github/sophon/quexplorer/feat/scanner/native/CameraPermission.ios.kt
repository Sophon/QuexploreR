package io.github.sophon.quexplorer.feat.scanner.native

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
@Composable
internal actual fun rememberCameraPermission(): CameraPermissionHandle {
    val statusState = remember { mutableStateOf(currentStatus()) }
    return remember {
        object : CameraPermissionHandle {
            override val status: CameraPermission
                get() {
                    return statusState.value
                }

            override fun request() {
                // If already answered, requestAccess calls back immediately with the
                // existing decision — that's the desired no-op-then-update behaviour.
                AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                    // Completion handler comes back on an arbitrary queue. Bounce to
                    // main so the State write lands on the thread Compose reads from.
                    dispatch_async(dispatch_get_main_queue()) {
                        statusState.value = if (granted) {
                            CameraPermission.Granted
                        } else {
                            CameraPermission.Denied
                        }
                    }
                }
            }
        }
    }
}

private fun currentStatus(): CameraPermission {
    val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
    return when (status) {
        AVAuthorizationStatusAuthorized -> CameraPermission.Granted
        AVAuthorizationStatusNotDetermined -> CameraPermission.NotAsked
        AVAuthorizationStatusDenied -> CameraPermission.Denied
        AVAuthorizationStatusRestricted -> CameraPermission.Denied
        else -> CameraPermission.Denied
    }
}
