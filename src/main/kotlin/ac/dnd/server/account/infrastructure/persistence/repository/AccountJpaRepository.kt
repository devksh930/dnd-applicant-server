package ac.dnd.server.account.infrastructure.persistence.repository

import ac.dnd.server.account.infrastructure.persistence.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountJpaRepository : JpaRepository<Account, Long> {
    fun findByEmail(email: String): Account?
}