package ac.dnd.server.admission.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ac.dnd.server.admission.application.dto.EventCreateCommand;
import ac.dnd.server.admission.application.service.EventCreateService;
import ac.dnd.server.admission.presentation.dto.request.EventCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventCreateController {
	private final EventCreateService eventCreateService;

	@PostMapping
	public ResponseEntity<Void> createEvent(
		@Valid @RequestBody final EventCreateRequest request
	) {
		final EventCreateCommand command = new EventCreateCommand(
			request.name(),
			request.startDateTime(),
			request.endDateTime()
		);

		final Long createEvent = eventCreateService.createEvent(command);

		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri()
			.path("/{id}")
			.buildAndExpand(createEvent)
			.toUri()).build();
	}
}
