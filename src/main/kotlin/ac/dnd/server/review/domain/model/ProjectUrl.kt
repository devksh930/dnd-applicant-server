package ac.dnd.server.review.domain.model

import ac.dnd.server.review.domain.enums.UrlType

data class ProjectUrl(
    val id: Long = 0L,
    val type: UrlType,
    val link: String,
    val order: Int
)
