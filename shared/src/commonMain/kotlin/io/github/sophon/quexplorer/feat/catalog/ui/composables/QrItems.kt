package io.github.sophon.quexplorer.feat.catalog.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.sophon.quexplorer.feat.qr.model.Qr

@Composable
internal fun QrItem(
    qr: Qr,
    modifier: Modifier = Modifier,
) {
    when (qr) {
        is Qr.Url -> QrUrlItem(qr = qr, modifier = modifier)
        is Qr.Text -> QrTextItem(qr = qr, modifier = modifier)
        is Qr.Wifi -> QrWifiItem(qr = qr, modifier = modifier)
        is Qr.Contact -> QrContactItem(qr = qr, modifier = modifier)
        is Qr.Geo -> QrGeoItem(qr = qr, modifier = modifier)
        is Qr.Email -> QrEmailItem(qr = qr, modifier = modifier)
        is Qr.Phone -> QrPhoneItem(qr = qr, modifier = modifier)
    }
}

@Composable
private fun QrItemRow(
    icon: ImageVector,
    name: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name,
            modifier = Modifier.padding(end = 16.dp),
        )
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
            content()
        }
    }
}

@Composable
private fun QrUrlItem(
    qr: Qr.Url,
    modifier: Modifier = Modifier,
) {
    QrItemRow(icon = Icons.Default.Link, name = "URL", modifier = modifier) {
        Text(
            text = qr.url,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun QrTextItem(
    qr: Qr.Text,
    modifier: Modifier = Modifier,
) {
    QrItemRow(icon = Icons.Default.Description, name = "Text", modifier = modifier) {
        Text(
            text = qr.rawData,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun QrWifiItem(
    qr: Qr.Wifi,
    modifier: Modifier = Modifier,
) {
    QrItemRow(icon = Icons.Default.Wifi, name = "Wi-Fi", modifier = modifier) {
        Text(
            text = qr.ssid,
            style = MaterialTheme.typography.bodyMedium,
        )
        val suffix = if (qr.isHidden) " · hidden" else ""
        Text(
            text = "${qr.encryption.name}$suffix",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun QrContactItem(
    qr: Qr.Contact,
    modifier: Modifier = Modifier,
) {
    QrItemRow(icon = Icons.Default.Person, name = "Contact", modifier = modifier) {
        val primary = qr.name ?: qr.organization ?: qr.email ?: qr.phone ?: "—"
        Text(
            text = primary,
            style = MaterialTheme.typography.bodyMedium,
        )
        val secondary = listOfNotNull(qr.organization.takeIf { it != primary }, qr.phone, qr.email)
            .firstOrNull { it.isBlank().not() }
        if (secondary != null) {
            Text(
                text = secondary,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun QrGeoItem(
    qr: Qr.Geo,
    modifier: Modifier = Modifier,
) {
    QrItemRow(icon = Icons.Default.LocationOn, name = "Location", modifier = modifier) {
        Text(
            text = qr.query ?: "${qr.latitude}, ${qr.longitude}",
            style = MaterialTheme.typography.bodyMedium,
        )
        if (qr.query != null) {
            Text(
                text = "${qr.latitude}, ${qr.longitude}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun QrEmailItem(
    qr: Qr.Email,
    modifier: Modifier = Modifier,
) {
    QrItemRow(icon = Icons.Default.Email, name = "Email", modifier = modifier) {
        Text(
            text = qr.address,
            style = MaterialTheme.typography.bodyMedium,
        )
        if (qr.subject.isNullOrBlank().not()) {
            Text(
                text = qr.subject!!,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun QrPhoneItem(
    qr: Qr.Phone,
    modifier: Modifier = Modifier,
) {
    QrItemRow(icon = Icons.Default.Phone, name = "Phone", modifier = modifier) {
        Text(
            text = qr.number,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}