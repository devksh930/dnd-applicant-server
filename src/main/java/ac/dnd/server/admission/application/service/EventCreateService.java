package ac.dnd.server.admission.application.service;

import org.springframework.stereotype.Service;

import ac.dnd.server.admission.application.EventCreateCommand;
import ac.dnd.server.admission.domain.AdmissionRepository;
import ac.dnd.server.admission.domain.Event;
import ac.dnd.server.admission.domain.ViewablePeriod;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventCreateService {

	private final AdmissionRepository admissionRepository;

	public Long createEvent(
		final EventCreateCommand command
	) {
		return admissionRepository.saveEvent(new Event(command.name(),
			ViewablePeriod.of(
				command.startDateTime(),
				command.endDateTime()
			)
		));
	}
}
