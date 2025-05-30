package ac.dnd.server.admission.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ac.dnd.server.admission.domain.model.EventData;
import ac.dnd.server.admission.domain.AdmissionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventQueryService {
	private final AdmissionRepository admissionRepository;

	public List<EventData> getEvents() {
		return admissionRepository.getEvents();
	}

}
