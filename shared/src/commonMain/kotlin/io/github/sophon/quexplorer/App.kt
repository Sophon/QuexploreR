package io.github.sophon.quexplorer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import io.github.sophon.quexplorer.core.ui.PlaceholderScreen
import io.github.sophon.quexplorer.feat.scanner.ui.ScannerScreen
import io.github.sophon.quexplorer.navigation.model.Destination
import io.github.sophon.quexplorer.navigation.ui.BottomBar
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Destination.Scanner::class, Destination.Scanner.serializer())
            subclass(Destination.Catalog::class, Destination.Catalog.serializer())
        }
    }
}
val LocalBottomBarPadding = compositionLocalOf { PaddingValues(0.dp) }

@Composable
@Preview
fun App() {
    MaterialTheme {
        val backStack = rememberNavBackStack(navConfig, Destination.Catalog)

        val botPaddingValues = PaddingValues(
            bottom = 80.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        )

        CompositionLocalProvider(LocalBottomBarPadding provides botPaddingValues) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
            ) {
                AppNavDisplay(backStack = backStack)

                BottomBar(
                    current = (backStack.first() as Destination),
                    onTabClick = { destination ->
                        backStack.clear()
                        backStack.add(destination)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                )
            }
        }
    }
}

@Composable
private fun AppNavDisplay(
    backStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier
) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        entryProvider = entryProvider {
            entry<Destination.Catalog> {
                PlaceholderScreen(label = "Catalog")
            }

            entry<Destination.Scanner> {
                ScannerScreen()
            }
        }
    )
}
