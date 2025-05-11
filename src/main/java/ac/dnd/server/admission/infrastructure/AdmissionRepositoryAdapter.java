package ac.dnd.server.admission.infrastructure;

import java.util.List;

import org.springframework.stereotype.Repository;

import ac.dnd.server.admission.domain.AdmissionRepository;
import ac.dnd.server.admission.domain.Event;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AdmissionRepositoryAdapter implements AdmissionRepository {

	private final EventJpaRepository eventJpaRepository;
	private final ApplicantJpaRepository applicantJpaRepository;

	@Override
	public Long saveEvent(final Event event) {
		return eventJpaRepository.save(event)
			.getId();
	}

	@Override
	public List<Event> getEvents() {
		return eventJpaRepository.findAll();
	}
}
