package ac.dnd.server.admission.domain.event

import ac.dnd.server.admission.domain.enums.ApplicantStatus
import ac.dnd.server.admission.domain.enums.ApplicantType

data class ApplicantQueryEvent(
    val applicantId: Long,
    val name: String,
    val email: String,
    val type: ApplicantType,
    val status: ApplicantStatus
)
