package ac.dnd.server.shared.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secret: String = "",
    val accessTokenExpirationHours: Long = 1,
    val refreshTokenExpirationDays: Long = 30
)
