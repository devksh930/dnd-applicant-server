package ac.dnd.server.fixture;

import org.springframework.security.crypto.encrypt.TextEncryptor;

public class TextEncryptorFixture {
	public static TextEncryptor createNoOpTextEncryptor() {
		return new TextEncryptor() {
			@Override
			public String encrypt(String rawPassword) {
				return rawPassword;
			}

			@Override
			public String decrypt(final String encryptedText) {
				return encryptedText;
			}
		};
	}
}
