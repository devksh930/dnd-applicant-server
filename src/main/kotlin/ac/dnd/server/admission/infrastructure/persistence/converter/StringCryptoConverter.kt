package ac.dnd.server.admission.infrastructure.persistence.converter

import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.stereotype.Component
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
@Component
class StringCryptoConverter(
    private val textEncryptor: TextEncryptor
) : AttributeConverter<String, String> {

    override fun convertToDatabaseColumn(attribute: String?): String? {
        if (attribute == null) {
            return null
        }
        return try {
            textEncryptor.encrypt(attribute)
        } catch (e: Exception) {
            throw RuntimeException("Attribute encryption failed", e)
        }
    }

    override fun convertToEntityAttribute(dbData: String?): String? {
        if (dbData == null) {
            return null
        }
        return try {
            textEncryptor.decrypt(dbData)
        } catch (e: Exception) {
            throw RuntimeException("Attribute decryption failed", e)
        }
    }
}