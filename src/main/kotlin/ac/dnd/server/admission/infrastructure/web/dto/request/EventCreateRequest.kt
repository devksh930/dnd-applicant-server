package ac.dnd.server.admission.infrastructure.web.dto.request

import ac.dnd.server.shared.validation.StartBeforeEnd
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@StartBeforeEnd(startDateTime = "startDateTime", endDateTime = "endDateTime")
data class EventCreateRequest(
    @field:NotBlank
    val name: String,

    @field:NotNull
    val startDateTime: LocalDateTime,

    @field:NotNull
    val endDateTime: LocalDateTime
)
