package ac.dnd.server.admission.infrastructure;

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
}
