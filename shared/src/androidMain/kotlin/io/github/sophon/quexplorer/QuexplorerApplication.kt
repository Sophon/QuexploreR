package io.github.sophon.quexplorer

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent

internal class QuexplorerApplication: Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            Napier.base(DebugAntilog())
            androidContext(this@QuexplorerApplication)
        }
    }
}
