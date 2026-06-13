package io.github.sophon.quexplorer.core.model

internal sealed class Qr {
    abstract val data: String

    data class Url(override val data: String) : Qr()
    data class Text(override val data: String) : Qr()
    data class Wifi(override val data: String) : Qr()
    data class Contact(override val data: String) : Qr()
    data class Geo(override val data: String) : Qr()
    data class Email(override val data: String) : Qr()
    data class Phone(override val data: String) : Qr()
}
