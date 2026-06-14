package io.github.sophon.quexplorer

import androidx.compose.ui.window.ComposeUIViewController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import platform.UIKit.UIViewController

private var koinInitialized = false

fun MainViewController(): UIViewController {
    if (koinInitialized.not()) {
        Napier.base(DebugAntilog())
        initKoin()
        koinInitialized = true
    }
    return ComposeUIViewController {
        App()
    }
}
