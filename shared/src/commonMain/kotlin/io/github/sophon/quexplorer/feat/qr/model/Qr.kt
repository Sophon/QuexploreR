package io.github.sophon.quexplorer.feat.qr.model

internal sealed class Qr {
    abstract val rawData: String

    data class Url(
        override val rawData: String,
        val url: String,
    ): Qr()

    data class Text(
        override val rawData: String,
    ): Qr()

    data class Wifi(
        override val rawData: String,
        val ssid: String,
        val password: String,
        val encryption: Encryption,
        val isHidden: Boolean,
    ): Qr() {
        enum class Encryption { WPA, WEP, None }
    }

    data class Contact(
        override val rawData: String,
        val name: String?,
        val phone: String?,
        val email: String?,
        val organization: String?,
    ): Qr()

    data class Geo(
        override val rawData: String,
        val latitude: Double,
        val longitude: Double,
        val query: String?,
    ) : Qr()

    data class Email(
        override val rawData: String,
        val address: String,
        val subject: String?,
        val body: String?,
    ): Qr()

    data class Phone(
        override val rawData: String,
        val number: String,
    ): Qr()
}