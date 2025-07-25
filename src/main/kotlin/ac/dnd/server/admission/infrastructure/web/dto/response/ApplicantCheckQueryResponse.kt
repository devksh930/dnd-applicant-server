package ac.dnd.server.admission.infrastructure.web.dto.response

import ac.dnd.server.admission.domain.enums.ApplicantStatus

data class ApplicantCheckQueryResponse(
    val eventName: String,
    val name: String,
    val status: ApplicantStatus
)
