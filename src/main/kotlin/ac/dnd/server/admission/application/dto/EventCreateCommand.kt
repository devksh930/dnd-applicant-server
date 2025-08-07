package ac.dnd.server.admission.application.dto

import java.time.LocalDateTime

data class EventCreateCommand(
    val name: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
)