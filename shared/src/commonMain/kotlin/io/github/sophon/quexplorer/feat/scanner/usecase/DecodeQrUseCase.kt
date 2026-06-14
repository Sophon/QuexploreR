package io.github.sophon.quexplorer.feat.scanner.usecase

import io.github.sophon.quexplorer.feat.qr.QrParser
import io.github.sophon.quexplorer.feat.qr.model.Qr

internal class DecodeQrUseCase(
    private val parser: QrParser,
) {
    fun invoke(rawData: String): Qr {
        val result = parser.parse(rawData)
        return result
    }
}
