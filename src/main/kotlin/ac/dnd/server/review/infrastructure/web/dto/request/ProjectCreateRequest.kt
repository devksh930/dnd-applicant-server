package ac.dnd.server.review.infrastructure.web.dto.request

data class ProjectCreateRequest(
    val name: String,
    val description: String,
    val techStack: List<String>? = emptyList(),
    val urlLinks: List<UrlLinks>? = emptyList()
)
