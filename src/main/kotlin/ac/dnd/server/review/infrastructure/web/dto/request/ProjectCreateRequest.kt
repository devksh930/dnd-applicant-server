package ac.dnd.server.review.infrastructure.web.dto.request

import jakarta.validation.constraints.NotBlank

data class ProjectCreateRequest(
    @field:NotBlank(message = "프로젝트 이름은 필수입니다")
    val name: String,
    @field:NotBlank(message = "프로젝트 설명은 필수입니다")
    val description: String,
    val techStacks: List<String>? = null,
    val fileId: Long? = null,
    val urlLinks: List<UrlLinks>? = null
)
