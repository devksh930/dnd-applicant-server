package ac.dnd.server.admission.domain.model

import ac.dnd.server.admission.domain.enums.ApplicantStatus

data class ApplicantStatusData(
    val eventName: String,
    val name: String,
    val status: ApplicantStatus
)
