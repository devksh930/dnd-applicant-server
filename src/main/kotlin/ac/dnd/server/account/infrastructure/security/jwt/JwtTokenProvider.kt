package ac.dnd.server.account.infrastructure.security.jwt

import ac.dnd.server.shared.config.properties.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    private val jwtProperties: JwtProperties
) {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())

    fun generateAccessToken(userKey: UUID, email: String, role: String): String {
        return buildToken(
            subject = userKey.toString(),
            claims = mapOf(CLAIM_EMAIL to email, CLAIM_ROLE to role),
            expirationMillis = jwtProperties.accessTokenExpirationHours * 60 * 60 * 1000L
        )
    }

    fun generateRefreshToken(userKey: UUID): String {
        return buildToken(
            subject = userKey.toString(),
            claims = emptyMap(),
            expirationMillis = jwtProperties.refreshTokenExpirationDays * 24 * 60 * 60 * 1000L
        )
    }

    fun validateToken(token: String): Boolean {
        return try {
            parseClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun extractUserKey(token: String): UUID {
        return UUID.fromString(parseClaims(token).subject)
    }

    fun extractEmail(token: String): String {
        return parseClaims(token).get(CLAIM_EMAIL, String::class.java)
    }

    fun extractRole(token: String): String {
        return parseClaims(token).get(CLAIM_ROLE, String::class.java)
    }

    private fun buildToken(subject: String, claims: Map<String, Any>, expirationMillis: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + expirationMillis)

        return Jwts.builder()
            .subject(subject)
            .claims(claims)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey)
            .compact()
    }

    private fun parseClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    companion object {
        private const val CLAIM_EMAIL = "email"
        private const val CLAIM_ROLE = "role"
    }
}
