package ac.dnd.server.fixture

import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.security.crypto.encrypt.TextEncryptor

object TextEncryptorFixture {
    fun createNoOpTextEncryptor(): TextEncryptor {
        return Encryptors.noOpText()
    }
}