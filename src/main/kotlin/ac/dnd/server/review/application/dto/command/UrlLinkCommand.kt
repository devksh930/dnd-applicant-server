package ac.dnd.server.review.application.dto.command

import ac.dnd.server.review.domain.enums.UrlType

data class UrlLinkCommand(
    val type: UrlType,
    val url: String,
    val order: Int? = null
)
