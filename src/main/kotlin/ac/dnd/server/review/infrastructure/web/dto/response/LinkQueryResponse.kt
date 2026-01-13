package ac.dnd.server.review.infrastructure.web.dto.response

import ac.dnd.server.review.domain.enums.FormLinkType
import java.time.LocalDateTime

data class LinkQueryResponse(
    val type: FormLinkType,
    val expiredAt: LocalDateTime
)