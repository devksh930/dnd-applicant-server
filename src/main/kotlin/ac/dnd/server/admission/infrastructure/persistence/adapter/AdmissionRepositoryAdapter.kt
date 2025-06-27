package ac.dnd.server.admission.infrastructure.persistence.adapter

import ac.dnd.server.admission.domain.AdmissionRepository
import ac.dnd.server.admission.domain.model.ApplicantData
import ac.dnd.server.admission.domain.model.EventData
import ac.dnd.server.admission.domain.model.EventsData
import ac.dnd.server.admission.infrastructure.persistence.dto.ApplicantStatusDto
import ac.dnd.server.admission.infrastructure.persistence.mapper.ApplicantPersistenceMapper
import ac.dnd.server.admission.infrastructure.persistence.repository.ApplicantJpaRepository
import ac.dnd.server.admission.infrastructure.persistence.repository.EventJpaRepository
import ac.dnd.server.enums.EventStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class AdmissionRepositoryAdapter(
    private val eventJpaRepository: EventJpaRepository,
    private val applicantJpaRepository: ApplicantJpaRepository,
    private val applicantPersistenceMapper: ApplicantPersistenceMapper,
) : AdmissionRepository {

    private val log = LoggerFactory.getLogger(AdmissionRepositoryAdapter::class.java)

    override fun saveEvent(event: EventData): Long {
        val save = applicantPersistenceMapper.eventDataToEntity(event)
        return eventJpaRepository.save(save).id!!
    }

    override fun getEvents(): List<EventData> {
        return eventJpaRepository.findAll()
            .map { applicantPersistenceMapper.eventEntityToDomain(it) }
    }

    override fun findAdmissionByEventIdAndNameAndEmail(
        eventId: Long,
        name: String,
        email: String
    ): Optional<ApplicantData> {
        return applicantJpaRepository.findByEventIdAndNameAndEmail(
            eventId,
            name,
            email
        ).map { applicantPersistenceMapper.applicantEntityToDomain(it) }
    }

    override fun findApplicantStatusByEventIdAndNameAndEmail(
        eventId: Long,
        nameBlindIndex: String,
        emailBlindIndex: String
    ): Optional<ApplicantStatusDto> {
        return applicantJpaRepository.findApplicantStatusByEventIdAndNameAndEmail(
            eventId,
            nameBlindIndex,
            emailBlindIndex
        )
    }

    override fun findByStatusIn(statuses: List<EventStatus>): EventsData {
        return EventsData(
            eventJpaRepository.findByStatusIn(statuses)
                .map { applicantPersistenceMapper.eventEntityToDomain(it) }
        )
    }
}