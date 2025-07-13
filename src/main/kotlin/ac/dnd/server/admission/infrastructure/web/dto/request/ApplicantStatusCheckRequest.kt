package ac.dnd.server.admission.infrastructure.web.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class ApplicantStatusCheckRequest(
    @field:NotBlank
    val name: String,
    
    @field:Email
    @field:NotBlank
    val email: String
)