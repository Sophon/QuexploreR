package io.github.sophon.quexplorer.feat.qr.data

import io.github.sophon.quexplorer.core.arch.DataError
import io.github.sophon.quexplorer.core.arch.EmptyResult
import io.github.sophon.quexplorer.feat.qr.model.QrEntry
import kotlinx.coroutines.flow.Flow

internal interface QrDatabase {
    suspend fun saveQr(qrEntry: QrEntry): EmptyResult<DataError.Local>
    fun subscribeToEntries(): Flow<List<QrEntry>>
    suspend fun deleteQr(id: String): EmptyResult<DataError.Local>
    suspend fun wipe(): EmptyResult<DataError.Local>
}
