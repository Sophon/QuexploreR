package io.github.sophon.quexplorer.feat

import io.github.sophon.quexplorer.feat.scanner.ui.ScannerVM
import io.github.sophon.quexplorer.core.usecase.DecodeQrDataUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal fun featureModule() = module {
    //region SCANNER
    viewModelOf(::ScannerVM)
    singleOf(::DecodeQrDataUseCase)
    //endregion
}
