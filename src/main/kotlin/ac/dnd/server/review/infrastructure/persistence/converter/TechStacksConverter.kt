package ac.dnd.server.review.infrastructure.persistence.converter

import ac.dnd.server.review.domain.value.TechStacks
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class TechStacksConverter : AttributeConverter<TechStacks, String> {

    override fun convertToDatabaseColumn(attribute: TechStacks?): String {
        return attribute?.values?.joinToString(DELIMITER) ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): TechStacks {
        if (dbData.isNullOrBlank()) {
            return TechStacks()
        }
        return TechStacks(dbData.split(DELIMITER))
    }

    companion object {
        private const val DELIMITER = ","
    }
}
