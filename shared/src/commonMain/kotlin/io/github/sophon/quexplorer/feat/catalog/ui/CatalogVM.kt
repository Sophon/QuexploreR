package io.github.sophon.quexplorer.feat.catalog.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.sophon.quexplorer.feat.catalog.usecase.SubscribeToQrEntriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class CatalogVM(
    private val subscribeToQrEntriesUseCase: SubscribeToQrEntriesUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(CatalogState())
    val state: StateFlow<CatalogState> = _state
        .onStart { observeEntries() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CatalogState(),
        )


    fun deleteEntry(index: Int) {
        // TODO: wire delete use case
    }


    private fun observeEntries() {
        viewModelScope.launch {
            subscribeToQrEntriesUseCase.invoke()
                .collectLatest { qrEntryList ->
                    _state.update { it.copy(qrEntryList = qrEntryList) }
                }
        }
    }
}
