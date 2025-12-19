package ac.dnd.server.review.infrastructure.web.dto.response

data class ProjectInitResponse(
    val generation: String,
    val teamCount: Int,
    val links: List<ProjectInitLink>
)

data class ProjectInitLink(
    val teamName: String,
    val linkKey: String
)
