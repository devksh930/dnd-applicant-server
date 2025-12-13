package ac.dnd.server.fixture

import ac.dnd.server.shared.config.properties.JwtProperties

object JwtPropertiesFixture {

    private const val DEFAULT_SECRET = "test-secret-key-for-jwt-token-generation-must-be-at-least-256-bits"
    private const val DEFAULT_ACCESS_TOKEN_EXPIRATION_HOURS = 1L
    private const val DEFAULT_REFRESH_TOKEN_EXPIRATION_DAYS = 30L

    fun create(
        secret: String = DEFAULT_SECRET,
        accessTokenExpirationHours: Long = DEFAULT_ACCESS_TOKEN_EXPIRATION_HOURS,
        refreshTokenExpirationDays: Long = DEFAULT_REFRESH_TOKEN_EXPIRATION_DAYS,
        cookieSecure: Boolean = false,
        cookieSameSite: String = "Lax"
    ): JwtProperties {
        return JwtProperties(
            secret = secret,
            accessTokenExpirationHours = accessTokenExpirationHours,
            refreshTokenExpirationDays = refreshTokenExpirationDays,
            cookie = JwtProperties.CookieProperties(
                secure = cookieSecure,
                sameSite = cookieSameSite
            )
        )
    }

    fun createWithShortExpiration(): JwtProperties {
        return create(
            accessTokenExpirationHours = 1L,
            refreshTokenExpirationDays = 1L
        )
    }
}