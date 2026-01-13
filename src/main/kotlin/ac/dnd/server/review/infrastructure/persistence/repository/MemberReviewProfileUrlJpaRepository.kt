package ac.dnd.server.review.infrastructure.persistence.repository

import ac.dnd.server.review.infrastructure.persistence.entity.MemberReviewProfileUrl
import org.springframework.data.jpa.repository.JpaRepository

interface MemberReviewProfileUrlJpaRepository : JpaRepository<MemberReviewProfileUrl, Long>
