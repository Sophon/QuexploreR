package io.github.sophon.quexplorer.feat.scanner.ui

import androidx.lifecycle.ViewModel
import io.github.sophon.quexplorer.feat.qr.QrParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class ScannerVM(
    private val qrParser: QrParser,
): ViewModel() {
    private val _state = MutableStateFlow(ScannerState())
    val state = _state.asStateFlow()


    fun onDetectQr(data: String) {
        val qr = qrParser.parse(rawData = data)
        _state.update {
            it.copy(preview = ScannerState.Preview(isVisible = true, qr = qr))
        }
    }

    fun onCapture() {
        //TODO: save the data
        onDismissPreview()
    }

    fun onDismissPreview() {
        _state.update { it.copy(preview = ScannerState.Preview(isVisible = false)) }
    }
}