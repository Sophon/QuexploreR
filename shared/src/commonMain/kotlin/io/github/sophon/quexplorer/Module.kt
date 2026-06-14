package io.github.sophon.quexplorer

import io.github.sophon.quexplorer.feat.featureModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration


internal fun initKoin(config: KoinAppDeclaration? = null) = startKoin {
    config?.invoke(this)

    modules(
        platformModule,
        featureModule(),
    )
}

internal expect val platformModule: Module