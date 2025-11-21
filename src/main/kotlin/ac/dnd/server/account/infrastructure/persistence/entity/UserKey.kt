package ac.dnd.server.account.infrastructure.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.*

@Embeddable
data class UserKey(
    @Column(
        name = "user_key",
        nullable = false,
        unique = true,
        updatable = false,
        columnDefinition = "BINARY(16)"
    )
    val value: UUID = UUID.randomUUID()
) : Serializable {
    companion object {
        fun generate(): UserKey = UserKey(UUID.randomUUID())
    }
}
