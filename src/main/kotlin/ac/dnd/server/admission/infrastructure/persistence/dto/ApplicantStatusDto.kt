package ac.dnd.server.admission.infrastructure.persistence.dto

import ac.dnd.server.admission.domain.enums.ApplicantStatus

data class ApplicantStatusDto(
    val eventName: String,
    val name: String,
    val status: ApplicantStatus
)
