package ac.dnd.server.admission.infrastructure.web.dto.response

import ac.dnd.server.enums.ApplicantStatus

data class ApplicantCheckQueryResponse(
    val eventName: String,
    val name: String,
    val status: ApplicantStatus
)