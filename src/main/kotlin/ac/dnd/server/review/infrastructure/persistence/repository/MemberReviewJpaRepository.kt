package ac.dnd.server.review.infrastructure.persistence.repository

import ac.dnd.server.review.infrastructure.persistence.entity.MemberReviewEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface MemberReviewJpaRepository : JpaRepository<MemberReviewEntity, Long> {
    fun findByGenerationInfoGenerationAndName(
        generation: String,
        name: String
    ): MemberReviewEntity?

    fun findByGenerationInfoGenerationAndGenerationInfoTeamName(
        generation: String,
        teamName: String
    ): List<MemberReviewEntity>

    @Query(
        """
        SELECT fl.expired as expired,
               fl.expirationDateTime as expirationDateTime,
               mr as memberReview
        FROM FormLinkEntity fl
        LEFT JOIN ProjectEntity p ON fl.targetId = p.id
        LEFT JOIN MemberReviewEntity mr ON mr.generationInfo.generation = p.generationInfo.generation
            AND mr.generationInfo.teamName = p.generationInfo.teamName
            AND mr.name = :name
        WHERE fl.key = :linkKey
        """
    )
    fun findMemberReviewWithLinkStatus(linkKey: UUID, name: String): MemberReviewLinkProjection?
}
