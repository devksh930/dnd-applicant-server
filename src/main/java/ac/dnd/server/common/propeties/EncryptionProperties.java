package ac.dnd.server.common.propeties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@ConfigurationProperties(prefix = "encryption")
@Getter
public class EncryptionProperties {

	private Aes aes;
	private Hmac hmac;

	// == Setter 직접 구현 == //
	public void setAes(Aes aes) {
		this.aes = aes;
	}

	public void setHmac(Hmac hmac) {
		this.hmac = hmac;
	}

	@Getter
	public static class Aes {
		private String password;
		private String salt;

		// == Setter 직접 구현 == //
		public void setPassword(String password) {
			this.password = password;
		}

		public void setSalt(String salt) {
			this.salt = salt;
		}
	}

	@Getter
	public static class Hmac {
		private String key;

		// == Setter 직접 구현 == //
		public void setKey(String key) {
			this.key = key;
		}
	}

}