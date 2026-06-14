package io.github.sophon.quexplorer.feat.qr

import io.github.sophon.quexplorer.feat.qr.model.Qr

internal class QrParser {
    fun parse(rawData: String): Qr {
        val result = when {
            rawData.startsWith("http://")
                    || rawData.startsWith("https://") -> Qr.Url(rawData = rawData, url = rawData)

            rawData.startsWith("WIFI:") -> rawData.parseWifi()
            rawData.startsWith("BEGIN:VCARD") -> rawData.parseVCard()
            rawData.startsWith("MECARD:") -> rawData.parseContact()
            rawData.startsWith("geo:") -> rawData.parseGeo()
            rawData.startsWith("mailto:") -> rawData.parseMail()
            rawData.startsWith("tel:") -> rawData.parsePhone()
            else -> Qr.Text(rawData = rawData)
        }
        return result
    }


    /**
     * `WIFI:T:WPA;S:MyNetwork;P:hunter2;H:false;;`
     */
    private fun String.parseWifi(): Qr.Wifi {
        val fields = this
            .removePrefix("WIFI:")
            .split(";")
            .filter { it.isNotBlank() }
            .mapNotNull { field ->
                val keyValuePair = field.split(":", limit = 2)
                if (keyValuePair.size == 2) {
                    keyValuePair[0] to keyValuePair[1]
                } else {
                    null
                }
            }
            .toMap()

        val encryption = when (fields["T"]) {
            "WPA" -> Qr.Wifi.Encryption.WPA
            "WEP" -> Qr.Wifi.Encryption.WEP
            else -> Qr.Wifi.Encryption.None
        }

        val result = Qr.Wifi(
            rawData = this,
            ssid = fields["S"].orEmpty(),
            password = fields["P"].orEmpty(),
            encryption = encryption,
            isHidden = (fields["H"] == "true"),
        )

        return result
    }

    /**
     * Multi-line, full vCard spec. Fields of interest: `FN:`, `TEL:`, `EMAIL:`, `ORG:`.
     *
     * ```
     * BEGIN:VCARD
     * VERSION:3.0
     * FN:John Smith
     * TEL;TYPE=CELL:+420123456789
     * EMAIL:john@example.com
     * ORG:Acme Corp
     * END:VCARD
     * ```
     */
    private fun String.parseVCard(): Qr.Contact {
        val fields: Map<String, String> = this
            .lines()
            .filter { it.isNotBlank() }
            .filterNot { it == "BEGIN:VCARD" || it == "END:VCARD" }
            .mapNotNull { line ->
                val colonIndex = line.indexOf(":")
                if (colonIndex < 0) return@mapNotNull null
                val key = line.substring(0, colonIndex).substringBefore(";")
                val value = line.substring(colonIndex + 1)
                key to value
            }
            .toMap()

        val name = fields["FN"]
            ?: fields["N"]?.split(";")?.let { parts ->
                val family = parts.getOrNull(0).orEmpty()
                val given = parts.getOrNull(1).orEmpty()
                listOf(given, family).filter { it.isNotBlank() }.joinToString(" ")
            }

        val result = Qr.Contact(
            rawData = this,
            name = name?.takeIf { it.isNotBlank() },
            phone = fields["TEL"],
            email = fields["EMAIL"],
            organization = fields["ORG"],
        )
        return result
    }

    /**
     * Bare: `tel:+420123456789`
     *
     * (IGNORED) Extension: `tel:+18005550199;ext=4242`
     *
     * (IGNORED) With params: `tel:+18005550199;ext=4242;phone-context=example.com`
     */
    private fun String.parsePhone(): Qr.Phone {
        val number = this
            .removePrefix("tel:")
            .substringBefore(";")
        val result = Qr.Phone(
            rawData = this,
            number = number,
        )
        return result
    }

    /**
     * `mailto:user@example.com?subject=Hello&body=Hi`
     */
    private fun String.parseMail(): Qr.Email {
        val withoutPrefix = this.removePrefix("mailto:")
        val address = withoutPrefix.substringBefore("?")
        val queryString = withoutPrefix.substringAfter("?", missingDelimiterValue = "")

        val params: Map<String, String> = queryString
            .split("&")
            .filter { it.isNotBlank() }
            .mapNotNull { param ->
                val keyValuePair = param.split("=", limit = 2)
                if (keyValuePair.size == 2) {
                    keyValuePair[0] to keyValuePair[1]
                } else {
                    null
                }
            }
            .toMap()

        val result = Qr.Email(
            rawData = this,
            address = address,
            subject = params["subject"],
            body = params["body"],
        )
        return result
    }

    /**
     * Format: `geo:LATITUDE,LONGITUDE[,ALTITUDE][?q=QUERY]`
     *
     * - `geo:50.0755,14.4378`
     * - `geo:50.0755,14.4378,235`
     * - `geo:50.0755,14.4378?q=Prague+Castle`
     * - `geo:0,0?q=Eiffel+Tower,+Paris` (placeholder coords, real intent in q)
     */
    private fun String.parseGeo(): Qr.Geo {
        val withoutPrefix = this.removePrefix("geo:")
        val coordsPart = withoutPrefix.substringBefore("?")
        val queryString = withoutPrefix.substringAfter("?", missingDelimiterValue = "")
        val parts = coordsPart.split(",")

        val latitude = parts.getOrNull(0)?.toDoubleOrNull() ?: 0.0
        val longitude = parts.getOrNull(1)?.toDoubleOrNull() ?: 0.0

        val query = queryString
            .split("&")
            .firstOrNull { it.startsWith("q=") }
            ?.removePrefix("q=")
            ?.takeIf { it.isNotBlank() }

        val result = Qr.Geo(
            rawData = this,
            latitude = latitude,
            longitude = longitude,
            query = query,
        )
        return result
    }

    /**
     * Single-line, semicolon-separated. `N:` uses comma between surname and given name
     * (unlike vCard `N:` which uses semicolons). Closes with `;;`.
     *
     * - `MECARD:N:Smith,John;TEL:+420123456789;EMAIL:john@example.com;ORG:Acme;;`
     * - `MECARD:N:Doe,Jane;TEL:+15550100;;`
     */
    private fun String.parseContact(): Qr.Contact {
        val fields: Map<String, String> = this
            .removePrefix("MECARD:")
            .split(";")
            .filter { it.isNotBlank() }
            .mapNotNull { field ->
                val keyValuePair = field.split(":", limit = 2)
                if (keyValuePair.size == 2) {
                    keyValuePair[0] to keyValuePair[1]
                } else {
                    null
                }
            }
            .toMap()

        val name = fields["N"]
            ?.split(",", limit = 2)
            ?.let { parts ->
                val family = parts.getOrNull(0).orEmpty()
                val given = parts.getOrNull(1).orEmpty()
                listOf(given, family).filter { it.isNotBlank() }.joinToString(" ")
            }
            ?.takeIf { it.isNotBlank() }

        val result = Qr.Contact(
            rawData = this,
            name = name,
            phone = fields["TEL"],
            email = fields["EMAIL"],
            organization = fields["ORG"],
        )
        return result
    }
}