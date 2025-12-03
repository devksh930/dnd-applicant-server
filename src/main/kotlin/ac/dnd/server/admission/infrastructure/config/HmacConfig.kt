package ac.dnd.server.admission.infrastructure.config

import ac.dnd.server.admission.infrastructure.persistence.crypto.HmacBlindIndexCreator
import ac.dnd.server.shared.config.properties.EncryptionProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HmacConfig(
    private val encryptionProperties: EncryptionProperties,
) {

    @Bean
    fun hmacBlindIndexCreator(): HmacBlindIndexCreator {
        return HmacBlindIndexCreator(encryptionProperties)
    }
}
