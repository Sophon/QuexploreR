package io.github.sophon.quexplorer.feat.scanner.native

import androidx.compose.runtime.Composable

internal enum class CameraPermission { NotAsked, Granted, Denied }

internal interface CameraPermissionHandle {
    val status: CameraPermission
    fun request()
}

@Composable
internal expect fun rememberCameraPermission(): CameraPermissionHandle
