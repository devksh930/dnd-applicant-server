package ac.dnd.server.admission.infrastructure.persistence.converter;

import org.springframework.stereotype.Component;

import ac.dnd.server.admission.infrastructure.persistence.crypto.HmacBlindIndexCreator;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
@Component
public class StringHmacConverter implements AttributeConverter<String, String> {

	private final HmacBlindIndexCreator hmacBlindIndexCreator;

	public StringHmacConverter(final HmacBlindIndexCreator hmacBlindIndexCreator) {
		this.hmacBlindIndexCreator = hmacBlindIndexCreator;
	}

	@Override
	public String convertToDatabaseColumn(final String attribute) {
		return hmacBlindIndexCreator.create(attribute);
	}

	@Override
	public String convertToEntityAttribute(final String dbData) {
		return dbData;
	}

}
