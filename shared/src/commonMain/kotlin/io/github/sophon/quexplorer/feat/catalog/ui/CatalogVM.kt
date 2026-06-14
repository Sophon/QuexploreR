package io.github.sophon.quexplorer.feat.catalog.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import io.github.sophon.quexplorer.core.arch.onError
import io.github.sophon.quexplorer.core.arch.onSuccess
import io.github.sophon.quexplorer.feat.catalog.usecase.DeleteQrEntryUseCase
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
    private val deleteQrEntryUseCase: DeleteQrEntryUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(CatalogState())
    val state: StateFlow<CatalogState> = _state
        .onStart { observeEntries() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CatalogState(),
        )


    fun onDeleteClick(id: String) {
        _state.update {
            it.copy(deleteConfirmationDialog = CatalogState.DeleteConfirmationDialog(id))
        }
    }

    fun onDismiss() {
        _state.update {
            it.copy(deleteConfirmationDialog = null)
        }
    }

    fun deleteEntry(id: String) {
        viewModelScope.launch {
            deleteQrEntryUseCase.invoke(id = id)
                .onSuccess {
                    _state.update { it.copy(deleteConfirmationDialog = null) }
                    //TODO: display success Toast
                }
                .onError {
                    Napier.e(tag = TAG) { "deleteEntry ($id): $it" }
                    _state.update { state -> state.copy(deleteConfirmationDialog = null) }
                }
        }
    }


    private fun observeEntries() {
        viewModelScope.launch {
            subscribeToQrEntriesUseCase.invoke()
                .collectLatest { qrEntryList ->
                    _state.update { it.copy(qrEntryList = qrEntryList) }
                }
        }
    }


    private companion object {
        const val TAG = "CatalogVM"
    }
}
