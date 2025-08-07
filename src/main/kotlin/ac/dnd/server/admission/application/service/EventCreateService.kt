package ac.dnd.server.admission.application.service

import ac.dnd.server.admission.application.dto.EventCreateCommand
import ac.dnd.server.admission.domain.AdmissionRepository
import org.springframework.stereotype.Service

@Service
class EventCreateService(
    private val admissionRepository: AdmissionRepository
) {
    // FIXME : resultDateTime이랑 status 추가해야함
    fun createEvent(command: EventCreateCommand): Long {
        TODO("create event")
    }
}
