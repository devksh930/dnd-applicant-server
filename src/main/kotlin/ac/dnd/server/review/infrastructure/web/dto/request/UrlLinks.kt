package ac.dnd.server.review.infrastructure.web.dto.request

import ac.dnd.server.review.domain.enums.UrlType
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.URL

data class UrlLinks(
    val type: UrlType,
    @field:NotBlank(message = "URL은 필수입니다")
    @field:URL(message = "올바른 URL 형식이 아닙니다")
    val url: String,
    val order: Int? = null
)
