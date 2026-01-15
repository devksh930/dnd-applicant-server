package ac.dnd.server.review.domain.repository

import ac.dnd.server.review.infrastructure.persistence.entity.MemberReviewEntity
import ac.dnd.server.review.infrastructure.persistence.entity.MemberReviewProfileUrlEntity

interface MemberReviewRepository {
    fun save(memberReview: MemberReviewEntity): MemberReviewEntity
    fun saveAllUrls(profileUrls: List<MemberReviewProfileUrlEntity>): List<MemberReviewProfileUrlEntity>
    fun deleteUrlsByMemberReviewId(memberReviewId: Long)
    fun findMemberReview(linkKey: String, name: String): MemberReviewEntity?
}
