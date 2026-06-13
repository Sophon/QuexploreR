package io.github.sophon.quexplorer.feat.scanner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            CameraPreview(
                onDetectQr = vm::onDetectQr,
                modifier = modifier.fillMaxSize(),
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
