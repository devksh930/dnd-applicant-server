package ac.dnd.server.admission.infrastructure.persistence.crypto

import ac.dnd.server.shared.config.properties.EncryptionProperties
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class HmacBlindIndexCreator(
    private val properties: EncryptionProperties
) {
    companion object {
        private const val HMAC_ALGORITHM = "HmacSHA256"
    }

    private val secretKeySpec: SecretKeySpec = SecretKeySpec(
        properties.hmac.key.toByteArray(StandardCharsets.UTF_8),
        HMAC_ALGORITHM
    )

    fun create(plainText: String): String {
        return try {
            val mac = Mac.getInstance(HMAC_ALGORITHM)
            mac.init(secretKeySpec)
            val hmacBytes = mac.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
            Base64.getEncoder().encodeToString(hmacBytes)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to create blind index", e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException("Failed to create blind index", e)
        }
    }
}
