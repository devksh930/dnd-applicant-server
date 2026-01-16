package ac.dnd.server.review.domain.model

import ac.dnd.server.review.domain.enums.FormLinkType
import java.time.LocalDateTime
import java.util.*

data class FormLink(
    val id: Long = 0L,
    val linkType: FormLinkType,
    val key: UUID,
    val targetId: Long,
    val expired: Boolean = false,
    val expirationDateTime: LocalDateTime = LocalDateTime.now().plusWeeks(2)
) {
    fun isExpired(): Boolean {
        if (expired) {
            return true
        }
        return expirationDateTime.isBefore(LocalDateTime.now())
    }
}
