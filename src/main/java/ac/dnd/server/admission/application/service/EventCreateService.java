package ac.dnd.server.admission.application.service;

import org.springframework.stereotype.Service;

import ac.dnd.server.admission.application.dto.EventCreateCommand;
import ac.dnd.server.admission.domain.AdmissionRepository;
import ac.dnd.server.admission.domain.model.EventData;
import ac.dnd.server.admission.domain.model.ViewablePeriod;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventCreateService {

	private final AdmissionRepository admissionRepository;

	//FIXME : resultDateTiem이랑 status 추가해야함
	public Long createEvent(
		final EventCreateCommand command
	) {
		return admissionRepository.saveEvent(new EventData(
			0L,
			command.name(),
			ViewablePeriod.of(
				command.startDateTime(),
				command.endDateTime()
			),
			null,
			null
		));
	}

}
