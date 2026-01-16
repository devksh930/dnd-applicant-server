package ac.dnd.server.review.infrastructure.web.mapper

import ac.dnd.server.review.domain.model.MemberReview
import ac.dnd.server.review.infrastructure.web.dto.response.MemberReviewProjectResponse
import ac.dnd.server.review.infrastructure.web.dto.response.MemberReviewResponse
import org.springframework.stereotype.Component

@Component
class MemberReviewWebMapper {
    fun toResponse(memberReview: MemberReview): MemberReviewResponse {
        return MemberReviewResponse(
            id = memberReview.id,
            name = memberReview.name,
            position = memberReview.position.name,
            status = memberReview.status.name
        )
    }

    fun toResponse(
        generation: String,
        teamName: String,
        members: List<MemberReview>
    ): MemberReviewProjectResponse {
        return MemberReviewProjectResponse(
            generation = generation,
            teamName = teamName,
            members = members.map { toResponse(it) }
        )
    }
}
