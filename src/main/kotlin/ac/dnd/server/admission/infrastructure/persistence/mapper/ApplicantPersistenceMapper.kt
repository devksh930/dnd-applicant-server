package ac.dnd.server.admission.infrastructure.persistence.mapper

import ac.dnd.server.admission.domain.model.ApplicantData
import ac.dnd.server.admission.domain.model.EventData
import ac.dnd.server.admission.infrastructure.persistence.entity.ApplicantEntity
import ac.dnd.server.admission.infrastructure.persistence.entity.EventEntity
import org.springframework.stereotype.Component

@Component
class ApplicantPersistenceMapper {

    fun eventDataToEntity(domain: EventData): EventEntity {
        return EventEntity(
            domain.name,
            domain.period,
            domain.resultAnnouncementDateTime,
            domain.status
        )
    }

    fun eventEntityToDomain(entity: EventEntity): EventData {
        return EventData(
            entity.id!!,
            entity.name,
            entity.period,
            entity.resultAnnouncementDateTime,
            entity.status
        )
    }

    fun applicantEntityToDomain(entity: ApplicantEntity): ApplicantData {
        return ApplicantData(
            entity.id!!,
            entity.event!!.name,
            entity.name,
            entity.email,
            entity.type,
            entity.status,
            entity.event!!.period
        )
    }
}
