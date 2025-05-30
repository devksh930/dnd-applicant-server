package ac.dnd.server.fixture;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class TextEncryptorFixture {
	public static TextEncryptor createNoOpTextEncryptor() {
		return Encryptors.noOpText();
	}
}
