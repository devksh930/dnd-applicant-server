package ac.dnd.server.fixture

import ac.dnd.server.account.infrastructure.persistence.entity.RefreshToken
import ac.dnd.server.account.infrastructure.persistence.entity.UserKey
import java.time.LocalDateTime

object RefreshTokenFixture {

    fun create(
        userKey: UserKey = UserKey.generate(),
        token: String = "test-refresh-token",
        expiresAt: LocalDateTime = LocalDateTime.now().plusDays(30),
        expired: Boolean = false
    ): RefreshToken {
        return RefreshToken(
            userKey = userKey,
            token = token,
            expiresAt = expiresAt,
            expired = expired
        )
    }

    fun createExpiredByTime(
        userKey: UserKey = UserKey.generate(),
        token: String = "expired-refresh-token"
    ): RefreshToken {
        return RefreshToken(
            userKey = userKey,
            token = token,
            expiresAt = LocalDateTime.now().minusDays(1),
            expired = false
        )
    }

    fun createExpiredByFlag(
        userKey: UserKey = UserKey.generate(),
        token: String = "revoked-refresh-token"
    ): RefreshToken {
        return RefreshToken(
            userKey = userKey,
            token = token,
            expiresAt = LocalDateTime.now().plusDays(30),
            expired = true
        )
    }

    fun createValid(
        userKey: UserKey = UserKey.generate(),
        token: String = "valid-refresh-token"
    ): RefreshToken {
        return RefreshToken(
            userKey = userKey,
            token = token,
            expiresAt = LocalDateTime.now().plusDays(30),
            expired = false
        )
    }
}
