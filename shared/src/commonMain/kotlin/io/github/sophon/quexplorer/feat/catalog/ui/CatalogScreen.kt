package io.github.sophon.quexplorer.feat.catalog.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.sophon.quexplorer.feat.catalog.ui.composables.QrItem
import io.github.sophon.quexplorer.feat.qr.model.QrEntry
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@Composable
internal fun CatalogScreen(
    modifier: Modifier = Modifier
) {
    val vm = koinViewModel<CatalogVM>()
    val state by vm.state.collectAsStateWithLifecycle()

    Content(
        state = state,
        onDeleteEntry = vm::deleteEntry,
        modifier = modifier,
    )
}

@Composable
private fun Content(
    state: CatalogState,
    onDeleteEntry: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        itemsIndexed(
            items = state.qrEntryList,
            key = { _, item -> item.id }
        ) { index, item ->
            Item(
                qrEntry = item,
                onDelete = { onDeleteEntry(index) }
            )
        }
    }
}

@Composable
private fun Item(
    qrEntry: QrEntry,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            QrItem(qr = qrEntry.qr)
            Text(
                text = formatRelativeTime(qrEntry.timeStamp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
            )
        }
    }
}

//TODO: this should prob be in `util`
private fun formatRelativeTime(timestamp: Long): String {
    val diff = Clock.System.now().toEpochMilliseconds() - timestamp
    return when {
        diff < 60_000L -> "just now"
        diff < 3_600_000L -> "${diff / 60_000L}m ago"
        diff < 86_400_000L -> "${diff / 3_600_000L}h ago"
        diff < 604_800_000L -> "${diff / 86_400_000L}d ago"
        else -> "${diff / 604_800_000L}w ago"
    }
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