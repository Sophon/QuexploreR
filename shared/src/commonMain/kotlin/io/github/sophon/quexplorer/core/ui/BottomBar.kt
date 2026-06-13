package io.github.sophon.quexplorer.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.sophon.quexplorer.navigation.Destination
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import quexplorer.shared.generated.resources.Res
import quexplorer.shared.generated.resources.app_destination_capture
import quexplorer.shared.generated.resources.app_destination_catalog

internal data class BottomBarItem(
    val label: StringResource,
    val icon: ImageVector,
    val destination: Destination,
)

private val bottomBarItems = listOf(
    BottomBarItem(
        label = Res.string.app_destination_catalog,
        icon = Icons.Default.Bookmark,
        destination = Destination.Catalog,
    ),
    BottomBarItem(
        label = Res.string.app_destination_capture,
        icon = Icons.Default.Camera,
        destination = Destination.Scanner,
    )
)


@Composable
internal fun BottomBar(
    current: Destination,
    onTabClick: (Destination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Transparent),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(48),
                )
                .padding(4.dp),
        ) {
            bottomBarItems.forEach { item ->
                BarItem(
                    item = item,
                    isSelected = (item.destination == current),
                    onClick = { onTabClick(item.destination) },
                    modifier = Modifier.widthIn(min = 80.dp),
                )
            }
        }
    }
}

@Composable
private fun BarItem(
    item: BottomBarItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val itemColor: Color
    val backgroundColor: Color

    if (isSelected) {
        itemColor = MaterialTheme.colorScheme.onSecondaryContainer
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer
    } else {
        itemColor = MaterialTheme.colorScheme.onSurfaceVariant
        backgroundColor = Color.Transparent
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(48))
            .clickable(enabled = true, onClick = onClick)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(48),
            )
            .padding(8.dp),
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = itemColor,
        )

        Text(
            text = stringResource(item.label),
            style = MaterialTheme.typography.labelSmall,
            color = itemColor,
        )
    }
}


//region PREVIEW
@Composable
@Preview(showBackground = true)
private fun BottomBarPreview() {
    MaterialTheme {
        BottomBar(
            current = Destination.Catalog,
            onTabClick = {},
        )
    }
}
//endregion