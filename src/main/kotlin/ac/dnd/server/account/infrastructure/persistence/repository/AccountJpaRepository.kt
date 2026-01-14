package ac.dnd.server.account.infrastructure.persistence.repository

import ac.dnd.server.account.infrastructure.persistence.entity.AccountEntity
import ac.dnd.server.account.infrastructure.persistence.entity.UserKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface AccountJpaRepository : JpaRepository<AccountEntity, Long> {
    fun findByEmail(email: String): AccountEntity?
    fun findByUserKey(userKey: UserKey): AccountEntity?

    @Query(
        """
        UPDATE AccountEntity a
        SET a.lastLoginAt = :lastLoginAt
        WHERE a.userKey = :userKey
        """
    )
    @Modifying(
        clearAutomatically = true,
        flushAutomatically = true
    )
    fun updateLastLoginDateByUserKey(
        @Param("userKey") userKey: UserKey,
        @Param("lastLoginAt") lastLoginAt: LocalDateTime = LocalDateTime.now()
    )
}