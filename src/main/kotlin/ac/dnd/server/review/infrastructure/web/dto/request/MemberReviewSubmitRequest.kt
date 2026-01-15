package ac.dnd.server.review.infrastructure.web.dto.request

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank

data class MemberReviewSubmitRequest(
    @field:NotBlank(message = "본인의 이름은 필수 입니다")
    val name: String,
    @field:NotBlank(message = "프로젝트 설명은 필수입니다")
    val description: String,
    @field:Valid
    val urlLinks: List<UrlLinks>? = null
)

