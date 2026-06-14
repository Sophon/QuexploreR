package io.github.sophon.quexplorer.feat.scanner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import io.github.sophon.quexplorer.core.arch.onError
import io.github.sophon.quexplorer.feat.scanner.usecase.CacheQrUseCase
import io.github.sophon.quexplorer.feat.scanner.usecase.DecodeQrUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ScannerVM(
    private val decodeQrUseCase: DecodeQrUseCase,
    private val cacheQrUseCase: CacheQrUseCase,
): ViewModel() {
    private val _state = MutableStateFlow(ScannerState())
    val state = _state.asStateFlow()


    fun onDetectQr(data: String) {
        val qr = decodeQrUseCase.invoke(rawData = data)
        _state.update {
            it.copy(preview = ScannerState.Preview(isVisible = true, qr = qr))
        }
    }

    fun onCapture() {
        _state.value.preview.qr?.let { qr ->
            viewModelScope.launch {
                cacheQrUseCase.invoke(qr).onError { Napier.e(tag = TAG) { it.toString() } }
            }
        }

        onDismissPreview()
    }

    fun onDismissPreview() {
        _state.update { it.copy(preview = ScannerState.Preview(isVisible = false)) }
    }


    private companion object {
        const val TAG = "ScannerVM"
    }
}
