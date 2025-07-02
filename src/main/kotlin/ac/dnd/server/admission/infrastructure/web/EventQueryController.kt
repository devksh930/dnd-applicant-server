package ac.dnd.server.admission.infrastructure.web

import ac.dnd.server.admission.application.service.EventQueryService
import ac.dnd.server.admission.infrastructure.web.dto.response.CurrentEventResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/events")
class EventQueryController(
    private val eventQueryService: EventQueryService
) {
    @GetMapping("/current")
    fun getCurrentEvent(): ResponseEntity<CurrentEventResponse> {
        val currentEvent = eventQueryService.getCurrentEvent()

        return ResponseEntity.ok(
            CurrentEventResponse(
                currentEvent.id,
                currentEvent.name,
                currentEvent.resultAnnouncementDateTime,
                currentEvent.isCompleted()
            )
        )
    }
}