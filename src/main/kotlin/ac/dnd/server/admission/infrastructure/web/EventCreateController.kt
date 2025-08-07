package ac.dnd.server.admission.infrastructure.web

import ac.dnd.server.admission.application.dto.EventCreateCommand
import ac.dnd.server.admission.application.service.EventCreateService
import ac.dnd.server.admission.infrastructure.web.dto.request.EventCreateRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/event")
class EventCreateController(
    private val eventCreateService: EventCreateService
) {
    @PostMapping
    fun createEvent(
        @Valid @RequestBody request: EventCreateRequest
    ): ResponseEntity<Void> {
        val command = EventCreateCommand(
            request.name,
            request.startDateTime,
            request.endDateTime
        )

        val createEvent = eventCreateService.createEvent(command)

        return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(createEvent)
                .toUri()
        ).build()
    }
}
