package ac.dnd.server.review.infrastructure.web.dto.request

import ac.dnd.server.review.domain.enums.UrlType

data class UrlLinks(
    val type: UrlType,
    val url: String,
    val order: Int? = null
)