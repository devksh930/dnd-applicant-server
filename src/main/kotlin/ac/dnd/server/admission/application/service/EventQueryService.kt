package ac.dnd.server.admission.application.service

import ac.dnd.server.admission.domain.AdmissionRepository
import ac.dnd.server.admission.domain.model.EventData
import ac.dnd.server.common.support.EventStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class EventQueryService(
    private val admissionRepository: AdmissionRepository
) {
    fun getEvents(): List<EventData> {
        return admissionRepository.getEvents()
    }

    fun getCurrentEvent(): EventData {
        return admissionRepository.findByStatusIn(
            listOf(
                EventStatus.PENDING,
                EventStatus.COMPLETED
            )
        ).findCurrentActiveEvent(LocalDateTime.now())
    }
}