package ac.dnd.server.review.infrastructure.persistence.repository

import ac.dnd.server.review.infrastructure.persistence.entity.MemberReviewEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberReviewJpaRepository : JpaRepository<MemberReviewEntity, Long>
