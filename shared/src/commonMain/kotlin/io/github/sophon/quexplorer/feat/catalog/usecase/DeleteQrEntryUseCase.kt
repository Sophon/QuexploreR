package io.github.sophon.quexplorer.feat.catalog.usecase

import io.github.sophon.quexplorer.core.arch.DataError
import io.github.sophon.quexplorer.core.arch.EmptyResult
import io.github.sophon.quexplorer.feat.qr.QrDatabase

internal class DeleteQrEntryUseCase(
    private val db: QrDatabase,
) {
    suspend fun invoke(id: String): EmptyResult<DataError.Local> {
        val result = db.deleteQr(id)
        return result
    }
}
