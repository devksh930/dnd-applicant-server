package ac.dnd.server.review.infrastructure.web.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class ProjectInitRequest(
    @field:NotBlank(message = "기수는 필수입니다")
    val generation: String,

    @field:Min(value = 1, message = "팀 개수는 1 이상이어야 합니다")
    val teamCount: Int
)
