package io.github.sophon.quexplorer.feat.scanner.usecase

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.sophon.quexplorer.core.model.Qr
import kotlin.test.Test

internal class DecodeQrDataUseCaseTest {
    private val usecase = DecodeQrDataUseCase()

    //region WIFI
    @Test
    fun `decoding handles normal wifi`() {
        // given
        val rawData = "WIFI:T:WPA;S:Wifi domaci;P:milujemepivo;H:false;;"
        val expected = Qr.Wifi(
            rawData = rawData,
            ssid = "Wifi domaci",
            password = "milujemepivo",
            encryption = Qr.Wifi.Encryption.WPA,
            isHidden = false,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles open network with nopass`() {
        // given
        val rawData = "WIFI:T:nopass;S:Guest;;"
        val expected = Qr.Wifi(
            rawData = rawData,
            ssid = "Guest",
            password = "",
            encryption = Qr.Wifi.Encryption.None,
            isHidden = false,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles WEP encryption`() {
        // given
        val rawData = "WIFI:T:WEP;S:OldRouter;P:abcd1234;H:false;;"
        val expected = Qr.Wifi(
            rawData = rawData,
            ssid = "OldRouter",
            password = "abcd1234",
            encryption = Qr.Wifi.Encryption.WEP,
            isHidden = false,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles hidden network`() {
        // given
        val rawData = "WIFI:T:WPA;S:Secret;P:hunter2;H:true;;"
        val expected = Qr.Wifi(
            rawData = rawData,
            ssid = "Secret",
            password = "hunter2",
            encryption = Qr.Wifi.Encryption.WPA,
            isHidden = true,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles wifi with missing password`() {
        // given
        val rawData = "WIFI:T:WPA;S:NoPasswordYet;;"
        val expected = Qr.Wifi(
            rawData = rawData,
            ssid = "NoPasswordYet",
            password = "",
            encryption = Qr.Wifi.Encryption.WPA,
            isHidden = false,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles wifi with fields in different order`() {
        // given
        val rawData = "WIFI:S:OrderTest;P:pass123;T:WPA;H:false;;"
        val expected = Qr.Wifi(
            rawData = rawData,
            ssid = "OrderTest",
            password = "pass123",
            encryption = Qr.Wifi.Encryption.WPA,
            isHidden = false,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles wifi password containing colon`() {
        // given
        val rawData = "WIFI:T:WPA;S:Net;P:pass:with:colons;H:false;;"
        val expected = Qr.Wifi(
            rawData = rawData,
            ssid = "Net",
            password = "pass:with:colons",
            encryption = Qr.Wifi.Encryption.WPA,
            isHidden = false,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles badly formatted wifi data`() {
        // given
        val rawData = "WIFI:garbage_no_colons;;"
        val expected = Qr.Wifi(
            rawData = rawData,
            ssid = "",
            password = "",
            encryption = Qr.Wifi.Encryption.None,
            isHidden = false,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }
    //endregion

    //region URL
    @Test
    fun `decoding handles http url`() {
        // given
        val rawData = "http://example.com"
        val expected = Qr.Url(rawData = rawData, url = rawData)

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles https url with path and query`() {
        // given
        val rawData = "https://example.com/path/to/page?foo=bar&baz=qux#section"
        val expected = Qr.Url(rawData = rawData, url = rawData)

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding treats uppercase scheme as text not url`() {
        // given
        val rawData = "HTTPS://EXAMPLE.COM"
        val expected = Qr.Text(rawData = rawData)

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }
    //endregion

    //region Text
    @Test
    fun `decoding handles plain text`() {
        // given
        val rawData = "Just some random text without any prefix"
        val expected = Qr.Text(rawData = rawData)

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles empty string as text`() {
        // given
        val rawData = ""
        val expected = Qr.Text(rawData = rawData)

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles vcalendar as text not contact`() {
        // given
        val rawData = "BEGIN:VCALENDAR\nVERSION:2.0\nEND:VCALENDAR"
        val expected = Qr.Text(rawData = rawData)

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles lowercase wifi prefix as text`() {
        // given
        val rawData = "wifi:T:WPA;S:Net;;"
        val expected = Qr.Text(rawData = rawData)

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }
    //endregion

    //region Geo
    @Test
    fun `decoding handles basic geo coordinates`() {
        // given
        val rawData = "geo:50.0755,14.4378"
        val expected = Qr.Geo(
            rawData = rawData,
            latitude = 50.0755,
            longitude = 14.4378,
            query = null,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles geo with altitude`() {
        // given
        val rawData = "geo:50.0755,14.4378,235"
        val expected = Qr.Geo(
            rawData = rawData,
            latitude = 50.0755,
            longitude = 14.4378,
            query = null,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles geo with query`() {
        // given
        val rawData = "geo:50.0755,14.4378?q=Prague+Castle"
        val expected = Qr.Geo(
            rawData = rawData,
            latitude = 50.0755,
            longitude = 14.4378,
            query = "Prague+Castle",
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles geo with placeholder coords and query`() {
        // given
        val rawData = "geo:0,0?q=Eiffel+Tower"
        val expected = Qr.Geo(
            rawData = rawData,
            latitude = 0.0,
            longitude = 0.0,
            query = "Eiffel+Tower",
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles negative geo coordinates`() {
        // given
        val rawData = "geo:-33.8688,-151.2093"
        val expected = Qr.Geo(
            rawData = rawData,
            latitude = -33.8688,
            longitude = -151.2093,
            query = null,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles malformed geo as zero coords`() {
        // given
        val rawData = "geo:not-a-number,also-bad"
        val expected = Qr.Geo(
            rawData = rawData,
            latitude = 0.0,
            longitude = 0.0,
            query = null,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }
    //endregion

    //region Phone
    @Test
    fun `decoding handles international phone number`() {
        // given
        val rawData = "tel:+420123456789"
        val expected = Qr.Phone(rawData = rawData, number = "+420123456789")

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles local phone without plus`() {
        // given
        val rawData = "tel:5550100"
        val expected = Qr.Phone(rawData = rawData, number = "5550100")

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding strips extension from phone number`() {
        // given
        val rawData = "tel:+18005550199;ext=4242"
        val expected = Qr.Phone(rawData = rawData, number = "+18005550199")

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding strips multiple phone parameters`() {
        // given
        val rawData = "tel:+18005550199;ext=4242;phone-context=example.com"
        val expected = Qr.Phone(rawData = rawData, number = "+18005550199")

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }
    //endregion

    //region Email
    @Test
    fun `decoding handles plain mailto`() {
        // given
        val rawData = "mailto:user@example.com"
        val expected = Qr.Email(
            rawData = rawData,
            address = "user@example.com",
            subject = null,
            body = null,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles mailto with subject only`() {
        // given
        val rawData = "mailto:user@example.com?subject=Hello"
        val expected = Qr.Email(
            rawData = rawData,
            address = "user@example.com",
            subject = "Hello",
            body = null,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles mailto with body only`() {
        // given
        val rawData = "mailto:user@example.com?body=Just%20a%20note"
        val expected = Qr.Email(
            rawData = rawData,
            address = "user@example.com",
            subject = null,
            body = "Just%20a%20note",
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles mailto with subject and body`() {
        // given
        val rawData = "mailto:user@example.com?subject=Hi&body=Hello%20there"
        val expected = Qr.Email(
            rawData = rawData,
            address = "user@example.com",
            subject = "Hi",
            body = "Hello%20there",
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding leaves url encoded characters untouched in email`() {
        // given
        val rawData = "mailto:user@example.com?subject=Caf%C3%A9%20visit"
        val expected = Qr.Email(
            rawData = rawData,
            address = "user@example.com",
            subject = "Caf%C3%A9%20visit",
            body = null,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }
    //endregion

    //region Contact (MECARD)
    @Test
    fun `decoding handles full mecard`() {
        // given
        val rawData = "MECARD:N:Smith,John;TEL:+420123456789;EMAIL:john@example.com;ORG:Acme;;"
        val expected = Qr.Contact(
            rawData = rawData,
            name = "John Smith",
            phone = "+420123456789",
            email = "john@example.com",
            organization = "Acme",
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles minimal mecard with name and phone`() {
        // given
        val rawData = "MECARD:N:Doe,Jane;TEL:+15550100;;"
        val expected = Qr.Contact(
            rawData = rawData,
            name = "Jane Doe",
            phone = "+15550100",
            email = null,
            organization = null,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles mecard with single name no comma`() {
        // given
        val rawData = "MECARD:N:Madonna;TEL:+15550100;;"
        val expected = Qr.Contact(
            rawData = rawData,
            name = "Madonna",
            phone = "+15550100",
            email = null,
            organization = null,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles mecard with only organization`() {
        // given
        val rawData = "MECARD:ORG:Acme Corp;;"
        val expected = Qr.Contact(
            rawData = rawData,
            name = null,
            phone = null,
            email = null,
            organization = "Acme Corp",
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles mecard with fields in different order`() {
        // given
        val rawData = "MECARD:EMAIL:e@x.com;ORG:Acme;TEL:+420;N:Doe,Jane;;"
        val expected = Qr.Contact(
            rawData = rawData,
            name = "Jane Doe",
            phone = "+420",
            email = "e@x.com",
            organization = "Acme",
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }
    //endregion

    //region Contact (vCard)
    @Test
    fun `decoding handles full vcard`() {
        // given
        val rawData = """
            BEGIN:VCARD
            VERSION:3.0
            FN:John Smith
            N:Smith;John;;;
            TEL:+420123456789
            EMAIL:john.smith@example.com
            ORG:Acme Corp
            END:VCARD
        """.trimIndent()
        val expected = Qr.Contact(
            rawData = rawData,
            name = "John Smith",
            phone = "+420123456789",
            email = "john.smith@example.com",
            organization = "Acme Corp",
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles vcard with TYPE parameter on TEL`() {
        // given
        val rawData = """
            BEGIN:VCARD
            VERSION:3.0
            FN:Jane Doe
            TEL;TYPE=CELL:+420987654321
            END:VCARD
        """.trimIndent()
        val expected = Qr.Contact(
            rawData = rawData,
            name = "Jane Doe",
            phone = "+420987654321",
            email = null,
            organization = null,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles vcard with only FN`() {
        // given
        val rawData = """
            BEGIN:VCARD
            VERSION:3.0
            FN:Just A Name
            END:VCARD
        """.trimIndent()
        val expected = Qr.Contact(
            rawData = rawData,
            name = "Just A Name",
            phone = null,
            email = null,
            organization = null,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding derives name from N when FN missing`() {
        // given
        val rawData = """
            BEGIN:VCARD
            VERSION:3.0
            N:Doe;Jane;;;
            TEL:+15550100
            END:VCARD
        """.trimIndent()
        val expected = Qr.Contact(
            rawData = rawData,
            name = "Jane Doe",
            phone = "+15550100",
            email = null,
            organization = null,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding keeps last TEL when vcard has multiple`() {
        // given
        val rawData = """
            BEGIN:VCARD
            VERSION:3.0
            FN:Multi Phone
            TEL:+11111
            TEL:+22222
            END:VCARD
        """.trimIndent()
        val expected = Qr.Contact(
            rawData = rawData,
            name = "Multi Phone",
            phone = "+22222",
            email = null,
            organization = null,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decoding handles vcard 4_0 version`() {
        // given
        val rawData = """
            BEGIN:VCARD
            VERSION:4.0
            FN:Newer Format
            EMAIL:new@example.com
            END:VCARD
        """.trimIndent()
        val expected = Qr.Contact(
            rawData = rawData,
            name = "Newer Format",
            phone = null,
            email = "new@example.com",
            organization = null,
        )

        // when
        val result = usecase.invoke(rawData)

        //then
        assertThat(result).isEqualTo(expected)
    }
    //endregion
}
