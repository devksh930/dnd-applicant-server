package ac.dnd.server.fixture

import ac.dnd.server.shared.config.properties.EncryptionProperties

object EncryptionPropertiesFixture {
    
    fun create(): EncryptionProperties {
        return EncryptionProperties(
            aes = EncryptionProperties.Aes(
                password = "test-password",
                salt = "test-salt"
            ),
            hmac = EncryptionProperties.Hmac(
                key = "test-hmac-key-for-testing-purposes"
            )
        )
    }
    
    fun createWithCustomHmacKey(hmacKey: String): EncryptionProperties {
        return EncryptionProperties(
            aes = EncryptionProperties.Aes(
                password = "test-password",
                salt = "test-salt"
            ),
            hmac = EncryptionProperties.Hmac(
                key = hmacKey
            )
        )
    }
    
    fun createWithEmptyHmacKey(): EncryptionProperties {
        return EncryptionProperties(
            aes = EncryptionProperties.Aes(
                password = "test-password",
                salt = "test-salt"
            ),
            hmac = EncryptionProperties.Hmac(
                key = ""
            )
        )
    }
}