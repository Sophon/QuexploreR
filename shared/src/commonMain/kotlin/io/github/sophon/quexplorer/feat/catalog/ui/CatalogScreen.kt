package io.github.sophon.quexplorer.feat.catalog.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun CatalogScreen(
    modifier: Modifier = Modifier
) {
    val vm = koinViewModel<CatalogVM>()
    val state by vm.state.collectAsStateWithLifecycle()

    Content(
        state = state,
        onDeleteEntry = vm::deleteEntry,
    )
}

@Composable
private fun Content(
    state: CatalogState,
    onDeleteEntry: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    //
}


//region PREVIEW
@Composable
@Preview(showBackground = true)
private fun CatalogPreview() {
    MaterialTheme {
        Content(
            state = CatalogState.PREVIEW,
            onDeleteEntry = {},
        )
    }
}
//endregion