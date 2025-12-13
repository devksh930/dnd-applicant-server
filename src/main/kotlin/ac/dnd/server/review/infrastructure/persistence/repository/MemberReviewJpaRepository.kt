package ac.dnd.server.review.infrastructure.persistence.repository

import ac.dnd.server.review.infrastructure.persistence.entity.MemberReview
import org.springframework.data.jpa.repository.JpaRepository

interface MemberReviewJpaRepository : JpaRepository<MemberReview, Long>
