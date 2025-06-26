package ac.dnd.server.admission.infrastructure.persistence.repository

import ac.dnd.server.admission.infrastructure.persistence.entity.Event
import ac.dnd.server.common.support.EventStatus
import org.springframework.data.jpa.repository.JpaRepository

interface EventJpaRepository : JpaRepository<Event, Long> {

    fun findByStatusIn(statuses: List<EventStatus>): List<Event>
}