package ac.dnd.server.admission.infrastructure.web;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ac.dnd.server.admission.application.service.EventQueryService;
import ac.dnd.server.admission.domain.model.EventData;
import ac.dnd.server.admission.infrastructure.web.dto.response.CurrentEventResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventQueryController {
	private final EventQueryService eventQueryService;

	@GetMapping("/current")
	public ResponseEntity<CurrentEventResponse> getCurrentEvent() {
		final EventData currentEvent = eventQueryService.getCurrentEvent();

		return ResponseEntity.ok(new CurrentEventResponse(
			currentEvent.id(),
			currentEvent.name(),
			currentEvent.resultAnnouncementDateTime(),
			currentEvent.isAnnounceable(LocalDateTime.now())
		));
	}

}
