package ac.dnd.server.account.infrastructure.persistence.repository

import ac.dnd.server.account.infrastructure.persistence.entity.Account
import ac.dnd.server.account.infrastructure.persistence.entity.UserKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface AccountJpaRepository : JpaRepository<Account, Long> {
    fun findByEmail(email: String): Account?

    @Query(
        """
        UPDATE Account a
        SET a.lastLoginAt = :lastLoginAt
        WHERE a.userKey = :userKey
        """
    )
    @Modifying(clearAutomatically = true)
    fun updateLastLoginDateByUserKey(
        @Param("userKey") userKey: UserKey,
        @Param("lastLoginAt") lastLoginAt: LocalDateTime = LocalDateTime.now()
    )
}