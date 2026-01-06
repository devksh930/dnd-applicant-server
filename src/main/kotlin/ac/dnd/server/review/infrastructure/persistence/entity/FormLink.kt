package ac.dnd.server.review.infrastructure.persistence.entity

import ac.dnd.server.review.domain.enums.FormLinkType
import ac.dnd.server.shared.persistence.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDateTime
import java.util.*

@Entity
class FormLink(
    @Enumerated(EnumType.STRING)
    val linkType: FormLinkType,
    @Column(name = "link_key")
    val key: UUID,
    val targetId: Long,
    val expired: Boolean = false,
    val expirationDateTime: LocalDateTime = LocalDateTime.now().plusWeeks(2)
) : BaseEntity() {

    fun isExpired(): Boolean {
        if (expired) {
            return true
        }
        return expirationDateTime.isBefore(LocalDateTime.now())
    }

}