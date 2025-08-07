package ac.dnd.server.admission.domain.model

import ac.dnd.server.admission.domain.enums.ApplicantStatus
import ac.dnd.server.admission.domain.enums.ApplicantType
import java.time.LocalDateTime

data class ApplicantData(
    val id: Long,
    val eventName: String,
    val name: String,
    val email: String,
    val type: ApplicantType,
    val status: ApplicantStatus,
    val period: ViewablePeriod
) {
    fun isViewable(now: LocalDateTime): Boolean {
        return period.contains(now)
    }
}
