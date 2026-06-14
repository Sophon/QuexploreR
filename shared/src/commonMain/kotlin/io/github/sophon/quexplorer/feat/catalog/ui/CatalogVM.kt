package io.github.sophon.quexplorer.feat.catalog.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class CatalogVM(): ViewModel() {
    private val _state = MutableStateFlow(CatalogState())
    val state = _state.asStateFlow()

    init {
        //TODO: load QR entries
    }


    fun deleteEntry(index: Int) {}


    private fun fetchData() {}
}
