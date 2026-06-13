package io.github.sophon.quexplorer.feat.scanner.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ScannerScreen(
    modifier: Modifier = Modifier,
) {
    val vm = koinViewModel<ScannerVM>()
    val state by vm.state.collectAsStateWithLifecycle()

    //content
}
