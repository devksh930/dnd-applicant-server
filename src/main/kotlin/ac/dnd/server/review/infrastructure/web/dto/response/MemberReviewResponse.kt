package ac.dnd.server.review.infrastructure.web.dto.response

data class MemberReviewResponse(
    val id: Long,
    val name: String,
    val position: String,
    val status: String
)

data class MemberReviewProjectResponse(
    val generation: String,
    val teamName: String,
    val members: List<MemberReviewResponse>
)
