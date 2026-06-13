package io.github.sophon.quexplorer.feat.scanner.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.sophon.quexplorer.feat.scanner.native.CameraPreview
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ScannerScreen(
    modifier: Modifier = Modifier,
) {
    val vm = koinViewModel<ScannerVM>()
    val state by vm.state.collectAsStateWithLifecycle()

    CameraPreview(
        onDetectQr = vm::onDetectQr,
        modifier = modifier.fillMaxSize(),
    )
}
