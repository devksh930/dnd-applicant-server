package ac.dnd.server.account.infrastructure.persistence.repository

import ac.dnd.server.account.infrastructure.persistence.entity.RefreshTokenEntity
import ac.dnd.server.account.infrastructure.persistence.entity.UserKey
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, Long> {
    fun findByToken(token: String): RefreshTokenEntity?
    fun findByUserKey(userKey: UserKey): RefreshTokenEntity?
    fun deleteByUserKey(userKey: UserKey)
}
