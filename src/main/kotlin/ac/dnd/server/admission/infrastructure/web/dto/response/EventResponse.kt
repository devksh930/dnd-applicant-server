package ac.dnd.server.admission.infrastructure.web.dto.response

import ac.dnd.server.admission.domain.model.ViewablePeriod

data class EventResponse(
    val id: Long,
    val name: String,
    val period: ViewablePeriod
)