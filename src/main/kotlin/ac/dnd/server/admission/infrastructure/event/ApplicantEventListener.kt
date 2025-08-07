package ac.dnd.server.admission.infrastructure.event

import ac.dnd.server.admission.domain.event.ApplicantQueryEvent
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ApplicantEventListener {
    private val log = LoggerFactory.getLogger(ApplicantEventListener::class.java)

    @EventListener(classes = [ApplicantQueryEvent::class])
    fun handleApplicantEvent(event: ApplicantQueryEvent) {
        log.info("Applicant query event: {}", event)
    }
}