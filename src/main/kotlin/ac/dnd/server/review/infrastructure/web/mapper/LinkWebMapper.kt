package ac.dnd.server.review.infrastructure.web.mapper

import ac.dnd.server.review.domain.model.FormLink
import ac.dnd.server.review.infrastructure.web.dto.response.LinkQueryResponse
import org.springframework.stereotype.Component

@Component
class LinkWebMapper {
    fun toResponse(formLink: FormLink): LinkQueryResponse {
        return LinkQueryResponse(
            type = formLink.linkType,
            expiredAt = formLink.expirationDateTime
        )
    }
}
