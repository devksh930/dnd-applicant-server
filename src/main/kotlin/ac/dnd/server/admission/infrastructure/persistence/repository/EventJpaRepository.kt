package ac.dnd.server.admission.infrastructure.persistence.repository

import ac.dnd.server.admission.infrastructure.persistence.entity.EventEntity
import ac.dnd.server.admission.domain.enums.EventStatus
import org.springframework.data.jpa.repository.JpaRepository

interface EventJpaRepository : JpaRepository<EventEntity, Long> {

    fun findByStatusIn(statuses: List<EventStatus>): List<EventEntity>
}
