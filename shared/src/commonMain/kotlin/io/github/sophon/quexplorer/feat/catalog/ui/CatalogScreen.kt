package io.github.sophon.quexplorer.feat.catalog.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.sophon.quexplorer.feat.catalog.ui.composables.QrItem
import io.github.sophon.quexplorer.feat.qr.model.QrEntry
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import quexplorer.shared.generated.resources.Res
import quexplorer.shared.generated.resources.app_catalog_dialog_delete_body
import quexplorer.shared.generated.resources.generic_no
import quexplorer.shared.generated.resources.generic_yes
import kotlin.time.Clock

@Composable
internal fun CatalogScreen(
    modifier: Modifier = Modifier
) {
    val vm = koinViewModel<CatalogVM>()
    val state by vm.state.collectAsStateWithLifecycle()

    Content(
        state = state,
        onDeleteClick = { vm.onDeleteClick(id = it) },
        onConfirmDelete = vm::deleteEntry,
        onDismiss = vm::onDismiss,
        modifier = modifier,
    )
}

@Composable
private fun Content(
    state: CatalogState,
    onDeleteClick: (String) -> Unit,
    onConfirmDelete: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            itemsIndexed(
                items = state.qrEntryList,
                key = { _, item -> item.id },
            ) { _, item ->
                Item(
                    qrEntry = item,
                    onDelete = { onDeleteClick(item.id) },
                )
            }
        }

        state.deleteConfirmationDialog?.let { dialog ->
            DeleteDialog(
                onConfirmDelete = { onConfirmDelete(dialog.id) },
                onDismiss = onDismiss,
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

@Composable
private fun DeleteDialog(
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(Res.string.app_catalog_dialog_delete_body))
        },
        confirmButton = {
            TextButton(onClick = onConfirmDelete) {
                Text(text = stringResource(Res.string.generic_yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.generic_no))
            }
        },
        modifier = modifier,
    )
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
            onDeleteClick = {},
            onConfirmDelete = {},
            onDismiss = {},
        )
    }
}
//endregion