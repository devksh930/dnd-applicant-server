package ac.dnd.server.review.domain.repository

import ac.dnd.server.review.domain.model.MemberReview
import ac.dnd.server.review.domain.model.MemberReviewProfileUrl

interface MemberReviewRepository {
    fun save(memberReview: MemberReview): MemberReview
    fun saveAllUrls(memberReviewId: Long, profileUrls: List<MemberReviewProfileUrl>)
    fun deleteUrlsByMemberReviewId(memberReviewId: Long)
    fun findMemberReview(linkKey: String, name: String): MemberReview?
    fun findByGenerationAndName(generation: String, name: String): MemberReview?
    fun findByGenerationAndTeamName(generation: String, teamName: String): List<MemberReview>
}