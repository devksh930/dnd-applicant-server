package ac.dnd.server.admission.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ac.dnd.server.admission.domain.AdmissionRepository;
import ac.dnd.server.admission.domain.model.EventData;
import ac.dnd.server.common.support.EventStatus;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventQueryService {
	private final AdmissionRepository admissionRepository;

	public List<EventData> getEvents() {
		return admissionRepository.getEvents();
	}

	public EventData getCurrentEvent() {
		return admissionRepository.findByStatusIn(List.of(
			EventStatus.PENDING,
			EventStatus.COMPLETED
		)).findCurrentActiveEvent(LocalDateTime.now());
	}
}
