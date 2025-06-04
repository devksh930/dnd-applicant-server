package ac.dnd.server.admission.infrastructure.persistence.crypto;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import ac.dnd.server.common.propeties.EncryptionProperties;

public class HmacBlindIndexCreator {

	private static final String HMAC_ALGORITHM = "HmacSHA256";
	private final EncryptionProperties properties;

	public HmacBlindIndexCreator(
		final EncryptionProperties properties
	) {
		this.properties = properties;
	}

	public String create(
		final String plainText
	) {
		try {
			Mac mac = Mac.getInstance(HMAC_ALGORITHM);
			SecretKeySpec secretKeySpec = new SecretKeySpec(
				properties.getHmac().getKey()
					.getBytes(StandardCharsets.UTF_8),
				HMAC_ALGORITHM
			);

			mac.init(secretKeySpec);
			byte[] hmacBytes = mac.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hmacBytes);

		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new RuntimeException(
				"Failed to create blind index",
				e
			);
		}
	}
}
