package io.github.sophon.quexplorer.feat.scanner.native

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
internal actual fun rememberCameraPermission(): CameraPermissionHandle {
    val context = LocalContext.current
    val statusState = remember { mutableStateOf(currentStatus(context)) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        statusState.value = if (granted) CameraPermission.Granted else CameraPermission.Denied
    }
    return remember(launcher) {
        object : CameraPermissionHandle {
            override val status: CameraPermission
                get() {
                    return statusState.value
                }

            override fun request() {
                launcher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}

private fun currentStatus(context: Context): CameraPermission {
    val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    return if (granted) CameraPermission.Granted else CameraPermission.NotAsked
}
