package ac.dnd.server.fixture

import ac.dnd.server.account.infrastructure.persistence.entity.RefreshTokenEntity
import ac.dnd.server.account.infrastructure.persistence.entity.UserKey
import java.time.LocalDateTime
import java.util.UUID

object RefreshTokenFixture {
    const val DEFAULT_TOKEN = "refresh-token"
    val DEFAULT_EXPIRES_AT = LocalDateTime.now().plusDays(7)

    fun createValid(
        userKey: UUID = UUID.randomUUID(),
        token: String = DEFAULT_TOKEN,
        expiresAt: LocalDateTime = DEFAULT_EXPIRES_AT
    ): RefreshTokenEntity {
        return RefreshTokenEntity(
            userKey = UserKey(userKey),
            token = token,
            expiresAt = expiresAt
        )
    }

    fun createExpiredByTime(
        userKey: UUID = UUID.randomUUID(),
        token: String = DEFAULT_TOKEN,
        expiresAt: LocalDateTime = LocalDateTime.now().minusDays(1)
    ): RefreshTokenEntity {
        return RefreshTokenEntity(
            userKey = UserKey(userKey),
            token = token,
            expiresAt = expiresAt
        )
    }

    fun createExpiredByFlag(
        userKey: UUID = UUID.randomUUID(),
        token: String = DEFAULT_TOKEN,
        expiresAt: LocalDateTime = DEFAULT_EXPIRES_AT
    ): RefreshTokenEntity {
        return RefreshTokenEntity(
            userKey = UserKey(userKey),
            token = token,
            expiresAt = expiresAt,
            expired = true
        )
    }

    fun create(
        userKey: UUID = UUID.randomUUID(),
        token: String = DEFAULT_TOKEN,
        expiresAt: LocalDateTime = DEFAULT_EXPIRES_AT,
        expired: Boolean = false
    ): RefreshTokenEntity {
        return RefreshTokenEntity(
            userKey = UserKey(userKey),
            token = token,
            expiresAt = expiresAt,
            expired = expired
        )
    }
}
