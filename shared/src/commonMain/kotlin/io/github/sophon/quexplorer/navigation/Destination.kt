package io.github.sophon.quexplorer.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

internal sealed interface Destination: NavKey {
    @Serializable
    data object Scanner: Destination

    @Serializable
    data object Catalog: Destination
}
