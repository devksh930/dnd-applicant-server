package ac.dnd.server.account.infrastructure.persistence.entity

import ac.dnd.server.shared.persistence.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "refresh_token")
class RefreshToken(
    @Embedded
    val userKey: UserKey,

    @Column(nullable = false, unique = true)
    var token: String,

    @Column(name = "expires_at", nullable = false)
    var expiresAt: LocalDateTime,

    var expired: Boolean = false
) : BaseEntity() {

    protected constructor() : this(UserKey.generate(), "", LocalDateTime.now())

    fun isExpired(): Boolean {
        return LocalDateTime.now().isAfter(expiresAt) || expired
    }

    fun updateToken(newToken: String, expiresAt: LocalDateTime) {
        this.token = newToken
        this.expiresAt = expiresAt
    }
}
