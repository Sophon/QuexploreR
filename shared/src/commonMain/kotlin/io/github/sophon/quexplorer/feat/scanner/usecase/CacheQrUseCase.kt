package io.github.sophon.quexplorer.feat.scanner.usecase

import io.github.sophon.quexplorer.core.arch.DataError
import io.github.sophon.quexplorer.core.arch.EmptyResult
import io.github.sophon.quexplorer.feat.qr.QrDatabase
import io.github.sophon.quexplorer.feat.qr.model.Qr
import io.github.sophon.quexplorer.feat.qr.model.QrEntry
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class CacheQrUseCase(
    private val db: QrDatabase,
) {
    suspend fun invoke(qr: Qr): EmptyResult<DataError.Local> {
        val qrEntry = QrEntry(
            id = Uuid.random().toString(),
            timeStamp = Clock.System.now().toEpochMilliseconds(),
            qr = qr,
        )

        val result = db.saveQr(qrEntry = qrEntry)
        return result
    }
}
