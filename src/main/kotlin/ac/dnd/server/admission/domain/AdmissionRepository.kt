package ac.dnd.server.admission.domain

import ac.dnd.server.admission.domain.model.ApplicantData
import ac.dnd.server.admission.domain.model.EventData
import ac.dnd.server.admission.domain.model.EventsData
import ac.dnd.server.common.support.EventStatus
import java.util.*

interface AdmissionRepository {
    fun saveEvent(event: EventData): Long

    fun getEvents(): List<EventData>

    fun findAdmissionByEventIdAndNameAndEmail(
        eventId: Long,
        name: String,
        email: String
    ): Optional<ApplicantData>

    fun findByStatusIn(statuses: List<EventStatus>): EventsData
}