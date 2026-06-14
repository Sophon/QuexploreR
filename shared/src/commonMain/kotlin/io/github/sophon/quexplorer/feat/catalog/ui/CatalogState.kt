package io.github.sophon.quexplorer.feat.catalog.ui

import io.github.sophon.quexplorer.feat.qr.model.Qr
import io.github.sophon.quexplorer.feat.qr.model.QrEntry

internal data class CatalogState(
    val qrEntryList: List<QrEntry> = emptyList(),
) {
    companion object {
        val PREVIEW = CatalogState(
            qrEntryList = listOf(
                QrEntry(
                    id = "1",
                    timeStamp = 1_749_900_000_000L,
                    qr = Qr.Url(
                        rawData = "https://anthropic.com",
                        url = "https://anthropic.com",
                    ),
                ),
                QrEntry(
                    id = "2",
                    timeStamp = 1_749_800_000_000L,
                    qr = Qr.Wifi(
                        rawData = "WIFI:T:WPA;S:HomeNetwork;P:hunter2;H:false;;",
                        ssid = "HomeNetwork",
                        password = "hunter2",
                        encryption = Qr.Wifi.Encryption.WPA,
                        isHidden = false,
                    ),
                ),
                QrEntry(
                    id = "3",
                    timeStamp = 1_749_700_000_000L,
                    qr = Qr.Email(
                        rawData = "mailto:martinfridrich@brainapptica.com?subject=Hello&body=Just%20saying%20hi",
                        address = "martinfridrich@brainapptica.com",
                        subject = "Hello",
                        body = "Just saying hi",
                    ),
                ),
                QrEntry(
                    id = "4",
                    timeStamp = 1_749_600_000_000L,
                    qr = Qr.Phone(
                        rawData = "tel:+420123456789",
                        number = "+420123456789",
                    ),
                ),
                QrEntry(
                    id = "5",
                    timeStamp = 1_749_500_000_000L,
                    qr = Qr.Geo(
                        rawData = "geo:50.0755,14.4378?q=Prague",
                        latitude = 50.0755,
                        longitude = 14.4378,
                        query = "Prague",
                    ),
                ),
                QrEntry(
                    id = "6",
                    timeStamp = 1_749_400_000_000L,
                    qr = Qr.Contact(
                        rawData = "BEGIN:VCARD\nVERSION:3.0\nFN:Jane Doe\nORG:Brainapptica\nTEL:+420987654321\nEMAIL:jane@example.com\nEND:VCARD",
                        name = "Jane Doe",
                        phone = "+420987654321",
                        email = "jane@example.com",
                        organization = "Brainapptica",
                    ),
                ),
                QrEntry(
                    id = "7",
                    timeStamp = 1_749_300_000_000L,
                    qr = Qr.Text(
                        rawData = "Just a plain text payload",
                    ),
                ),
            ),
        )
    }
}
