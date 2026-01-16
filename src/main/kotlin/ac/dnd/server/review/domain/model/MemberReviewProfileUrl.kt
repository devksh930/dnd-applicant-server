package ac.dnd.server.review.domain.model

import ac.dnd.server.review.domain.enums.UrlType

data class MemberReviewProfileUrl(
    val type: UrlType,
    val link: String,
    val order: Int
)
