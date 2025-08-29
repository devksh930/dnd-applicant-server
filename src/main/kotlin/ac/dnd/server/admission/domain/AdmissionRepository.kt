package ac.dnd.server.admission.domain

import ac.dnd.server.admission.domain.model.ApplicantData
import ac.dnd.server.admission.domain.model.EventData
import ac.dnd.server.admission.domain.model.EventsData
import ac.dnd.server.admission.domain.model.ApplicantStatusData
import ac.dnd.server.admission.domain.enums.EventStatus
import java.util.*

interface AdmissionRepository {
    fun saveEvent(event: EventData): Long

    fun getEvents(): List<EventData>

    fun findAdmissionByEventIdAndBlindIndexes(
        eventId: Long,
        nameBlindIndex: String,
        emailBlindIndex: String
    ): Optional<ApplicantData>

    fun findApplicantStatusByEventIdAndNameAndEmail(
        eventId: Long,
        nameBlindIndex: String,
        emailBlindIndex: String
    ): Optional<ApplicantStatusData>

    fun findByStatusIn(statuses: List<EventStatus>): EventsData
}
