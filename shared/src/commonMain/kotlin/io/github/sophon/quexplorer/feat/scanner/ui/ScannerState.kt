package io.github.sophon.quexplorer.feat.scanner.ui

import io.github.sophon.quexplorer.core.model.Qr

internal data class ScannerState(
    val preview: Preview = Preview(),
) {
    data class Preview(
        val isVisible: Boolean = false,
        val qr: Qr? = null,
    )
}