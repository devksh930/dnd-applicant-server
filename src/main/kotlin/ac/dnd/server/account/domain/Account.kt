package ac.dnd.server.account.domain

import ac.dnd.server.global.base.BaseEntity
import ac.dnd.server.enums.Role
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "account")
class Account(
    val name: String,
    val email: String,
    val password: String,
    val role: Role
) : BaseEntity() {
    
    protected constructor() : this("", "", "", Role.ADMIN)
}
