package io.github.sophon.quexplorer.feat.scanner.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class ScannerVM(): ViewModel() {
    private val _state = MutableStateFlow(ScannerState())
    val state = _state.asStateFlow()

    init {
        //TODO:
    }
}