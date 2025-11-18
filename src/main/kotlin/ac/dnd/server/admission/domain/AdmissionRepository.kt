package ac.dnd.server.admission.domain

import ac.dnd.server.admission.domain.enums.EventStatus
import ac.dnd.server.admission.domain.model.ApplicantData
import ac.dnd.server.admission.domain.model.ApplicantStatusData
import ac.dnd.server.admission.domain.model.EventData
import ac.dnd.server.admission.domain.model.EventsData

interface AdmissionRepository {
    fun saveEvent(event: EventData): Long

    fun getEvents(): List<EventData>

    fun findAdmissionByEventIdAndNameAndEmail(
        eventId: Long,
        name: String,
        email: String
    ): ApplicantData?

    fun findApplicantStatusByEventIdAndNameAndEmail(
        eventId: Long,
        nameBlindIndex: String,
        emailBlindIndex: String
    ): ApplicantStatusData?

    fun findByStatusIn(statuses: List<EventStatus>): EventsData
}
