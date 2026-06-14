package io.github.sophon.quexplorer.feat.scanner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.sophon.quexplorer.feat.qr.model.Qr
import io.github.sophon.quexplorer.feat.scanner.native.CameraPermission
import io.github.sophon.quexplorer.feat.scanner.native.CameraPreview
import io.github.sophon.quexplorer.feat.scanner.native.rememberCameraPermission
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ScannerScreen(
    modifier: Modifier = Modifier,
) {
    val vm = koinViewModel<ScannerVM>()
    val state by vm.state.collectAsStateWithLifecycle()
    val permission = rememberCameraPermission()

    LaunchedEffect(Unit) {
        if (permission.status == CameraPermission.NotAsked) {
            permission.request()
        }
    }

    when (permission.status) {
        CameraPermission.Granted -> {
            PreviewContent(
                preview = state.preview,
                onDetectQr = vm::onDetectQr,
                onCapture = vm::onCapture,
                onDismiss = vm::onDismissPreview,
            )
        }
        CameraPermission.NotAsked -> {
            Box(modifier = modifier.fillMaxSize())
        }
        CameraPermission.Denied -> {
            DeniedState(
                onRequest = permission::request,
                modifier = modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun PreviewContent(
    preview: ScannerState.Preview,
    onDetectQr: (data: String) -> Unit,
    onCapture: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        CameraPreview(
            onDetectQr = onDetectQr,
            modifier = Modifier.fillMaxSize(),
        )

        if (preview.isVisible) {
            preview.qr?.let { qr ->
                PreviewDialog(
                    qr = qr,
                    onCapture = onCapture,
                    onDismiss = onDismiss,
                )
            }
        }
    }
}

@Composable
private fun PreviewDialog(
    qr: Qr,
    onCapture: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.widthIn(max = 360.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = qr::class.simpleName.orEmpty(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(Modifier.height(12.dp))

                Text(
                    text = qr.rawData,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    FilledTonalIconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(56.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss",
                        )
                    }
                    FilledTonalIconButton(
                        onClick = onCapture,
                        modifier = Modifier.size(56.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save",
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DeniedState(
    onRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Camera access is required to scan QR codes.",
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRequest) {
            Text("Grant access")
        }
    }
}
