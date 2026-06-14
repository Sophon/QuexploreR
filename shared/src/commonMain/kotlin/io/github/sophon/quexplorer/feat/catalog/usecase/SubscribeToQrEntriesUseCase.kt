package io.github.sophon.quexplorer.feat.catalog.usecase

import io.github.sophon.quexplorer.feat.qr.QrDatabase
import io.github.sophon.quexplorer.feat.qr.model.QrEntry
import kotlinx.coroutines.flow.Flow

internal class SubscribeToQrEntriesUseCase(
    private val db: QrDatabase,
) {
    fun invoke(): Flow<List<QrEntry>> {
        val result = db.subscribeToEntries()
        return result
    }
}
