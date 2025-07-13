package ac.dnd.server.admission.infrastructure.persistence.converter

import ac.dnd.server.admission.infrastructure.persistence.crypto.HmacBlindIndexCreator
import org.springframework.stereotype.Component
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
@Component
class StringHmacConverter(
    private val hmacBlindIndexCreator: HmacBlindIndexCreator
) : AttributeConverter<String, String> {

    override fun convertToDatabaseColumn(attribute: String): String {
        return hmacBlindIndexCreator.create(attribute)
    }

    override fun convertToEntityAttribute(dbData: String): String {
        return dbData
    }
}