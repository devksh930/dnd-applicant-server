package ac.dnd.server.review.infrastructure.persistence.mapper

import ac.dnd.server.review.domain.model.MemberReview
import ac.dnd.server.review.infrastructure.persistence.entity.MemberReviewEntity
import org.springframework.stereotype.Component

@Component
class MemberReviewPersistenceMapper {

    fun toDomain(entity: MemberReviewEntity): MemberReview {
        return MemberReview(
            id = entity.id!!,
            generationInfo = entity.generationInfo,
            name = entity.name,
            position = entity.position,
            description = entity.description,
            status = entity.status,
            submittedAt = entity.submittedAt
        )
    }

    fun toEntity(domain: MemberReview): MemberReviewEntity {
        return MemberReviewEntity(
            generationInfo = domain.generationInfo,
            name = domain.name,
            position = domain.position,
            description = domain.description,
            status = domain.status,
            submittedAt = domain.submittedAt
        )
    }
}
