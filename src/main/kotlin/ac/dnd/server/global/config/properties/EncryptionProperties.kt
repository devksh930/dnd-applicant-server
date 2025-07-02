package ac.dnd.server.global.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "encryption")
data class EncryptionProperties(
    var aes: Aes = Aes(),
    var hmac: Hmac = Hmac()
) {
    data class Aes(
        var password: String = "",
        var salt: String = ""
    )

    data class Hmac(
        var key: String = ""
    )
}
