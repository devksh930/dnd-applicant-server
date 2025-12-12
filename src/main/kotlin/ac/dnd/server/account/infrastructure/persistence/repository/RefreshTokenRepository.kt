package ac.dnd.server.account.infrastructure.persistence.repository

import ac.dnd.server.account.infrastructure.persistence.entity.RefreshToken
import ac.dnd.server.account.infrastructure.persistence.entity.UserKey
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByToken(token: String): RefreshToken?
    fun findByUserKey(userKey: UserKey): RefreshToken?
    fun deleteByUserKey(userKey: UserKey)
}
