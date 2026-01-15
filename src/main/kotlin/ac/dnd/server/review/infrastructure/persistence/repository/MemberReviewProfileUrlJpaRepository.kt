package ac.dnd.server.review.infrastructure.persistence.repository

import ac.dnd.server.review.infrastructure.persistence.entity.MemberReviewProfileUrlEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberReviewProfileUrlJpaRepository : JpaRepository<MemberReviewProfileUrlEntity, Long> {
    fun deleteByMemberReviewId(memberReviewId: Long)
}
