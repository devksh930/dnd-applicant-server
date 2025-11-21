package ac.dnd.server.account.infrastructure.persistence.entity

import ac.dnd.server.shared.persistence.BaseEntity
import ac.dnd.server.account.domain.enums.Role
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import java.time.LocalDateTime

@Entity
@Table(name = "account")
class Account(
    @Embedded
    val userKey: UserKey = UserKey.generate(),
    val name: String,
    val email: String,
    val password: String,
    val role: Role,

    @Column(name = "last_login_at")
    var lastLoginAt: LocalDateTime? = null
) : BaseEntity() {

    protected constructor() : this(UserKey.generate(), "", "", "", Role.ADMIN, null)
}
