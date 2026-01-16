package ac.dnd.server.review.infrastructure.persistence.repository

import ac.dnd.server.review.infrastructure.persistence.entity.MemberReviewEntity
import java.time.LocalDateTime

interface MemberReviewLinkProjection {
    fun getExpired(): Boolean
    fun getExpirationDateTime(): LocalDateTime
    fun getMemberReview(): MemberReviewEntity?
}
