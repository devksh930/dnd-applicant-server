package ac.dnd.server.common.propeties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "encryption.text")
public record EncryptionTextProperties(
	String password,
	String salt
) {
	public EncryptionTextProperties {
		if (!isValidHex(password)) {
			throw new IllegalArgumentException("Encryption password must be a valid hexadecimal string.");
		}
		if (!isValidHex(salt)) {
			throw new IllegalArgumentException("Encryption salt must be a valid hexadecimal string.");
		}
	}

	// 16진수 문자열 유효성 검사 메서드
	private boolean isValidHex(String s) {
		if (s == null || s.isEmpty()) {
			return false;
		}
		return s.matches("^[0-9a-fA-F]+$");
	}

}
