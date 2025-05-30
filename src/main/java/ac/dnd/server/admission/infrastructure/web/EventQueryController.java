package ac.dnd.server.admission.infrastructure.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ac.dnd.server.admission.application.service.EventQueryService;
import ac.dnd.server.admission.infrastructure.web.dto.response.EventResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventQueryController {
	private final EventQueryService eventQueryService;

	@GetMapping
	public ResponseEntity<List<EventResponse>> getEvents() {

		final List<EventResponse> list = eventQueryService.getEvents()
			.stream()
			.map(query -> new EventResponse(
				query.id(),
				query.name(),
				query.period()
			)).toList();

		return ResponseEntity.ok(list);
	}

}
