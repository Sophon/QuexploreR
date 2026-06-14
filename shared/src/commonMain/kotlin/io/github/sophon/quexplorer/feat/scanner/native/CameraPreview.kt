package io.github.sophon.quexplorer.feat.scanner.native

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun CameraPreview(
    onDetectQr: (String) -> Unit,
    modifier: Modifier = Modifier,
)
