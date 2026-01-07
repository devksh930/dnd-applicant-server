package ac.dnd.server.review.application.dto.command

import ac.dnd.server.review.infrastructure.web.dto.request.UrlLinks

data class ProjectCreateCommand(
    val linkKey: String,
    val name: String,
    val description: String,
    val techStacks: List<String>?,
    val fileId: Long?,
    val urlLinks: List<UrlLinks>?
)
