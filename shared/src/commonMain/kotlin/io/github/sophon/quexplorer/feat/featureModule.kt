package io.github.sophon.quexplorer.feat

import io.github.sophon.quexplorer.feat.qr.QrParser
import io.github.sophon.quexplorer.feat.qr.data.MemoryQrDatabase
import io.github.sophon.quexplorer.feat.qr.data.QrDatabase
import io.github.sophon.quexplorer.feat.scanner.ui.ScannerVM
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal fun featureModule() = module {
    //region SCANNER
    viewModelOf(::ScannerVM)
    //endregion

    //region QR
    singleOf(::QrParser)
    singleOf(::MemoryQrDatabase).bind<QrDatabase>()
    //endregion
}
