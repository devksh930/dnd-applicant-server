package ac.dnd.server.admission.infrastructure.persistence.converter;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
@Component
public class StringCryptoConverter implements AttributeConverter<String, String> {

	private final TextEncryptor textEncryptor;

	public StringCryptoConverter(TextEncryptor textEncryptor) {
		this.textEncryptor = textEncryptor;
	}

	@Override
	public String convertToDatabaseColumn(String attribute) {
		if (attribute == null) {
			return null;
		}
		try {
			return textEncryptor.encrypt(attribute);
		} catch (Exception e) {
			throw new RuntimeException(
				"Attribute encryption failed",
				e
			);
		}
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		try {
			return textEncryptor.decrypt(dbData);
		} catch (Exception e) {
			throw new RuntimeException(
				"Attribute decryption failed",
				e
			);
		}
	}
}