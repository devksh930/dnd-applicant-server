package ac.dnd.server.shared.config.crypto

import ac.dnd.server.shared.config.properties.EncryptionProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.security.crypto.encrypt.TextEncryptor

@Configuration
class TextEncryptorConfig(
    private val encryptionProperties: EncryptionProperties,
) {

    @Bean
    fun textEncryptor(): TextEncryptor {
        return Encryptors.text(
            encryptionProperties.aes.password,
            encryptionProperties.aes.salt,
        )
    }
}
