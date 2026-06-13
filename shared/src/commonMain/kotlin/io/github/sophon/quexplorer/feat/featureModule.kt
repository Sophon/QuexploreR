package io.github.sophon.quexplorer.feat

import io.github.sophon.quexplorer.feat.scanner.ui.ScannerVM
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal fun featureModule() = module {
    viewModelOf(::ScannerVM)
}
