package io.github.sophon.quexplorer.feat.qr.data

import io.github.sophon.quexplorer.core.arch.DataError
import io.github.sophon.quexplorer.core.arch.EmptyResult
import io.github.sophon.quexplorer.core.arch.Result
import io.github.sophon.quexplorer.feat.qr.model.QrEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

internal class MemoryQrDatabase : QrDatabase {
    private val state = MutableStateFlow<Map<String, QrEntry>>(emptyMap())

    override suspend fun saveQr(qrEntry: QrEntry): EmptyResult<DataError.Local> {
        state.update { current -> current + (qrEntry.id to qrEntry) }
        return Result.Success(Unit)
    }

    override fun subscribeToEntries(): Flow<List<QrEntry>> {
        return state.map { entries ->
            entries.values.sortedByDescending { it.timeStamp }
        }
    }

    override suspend fun deleteQr(id: String): EmptyResult<DataError.Local> {
        state.update { current -> current - id }
        return Result.Success(Unit)
    }

    override suspend fun wipe(): EmptyResult<DataError.Local> {
        state.update { emptyMap() }
        return Result.Success(Unit)
    }
}
