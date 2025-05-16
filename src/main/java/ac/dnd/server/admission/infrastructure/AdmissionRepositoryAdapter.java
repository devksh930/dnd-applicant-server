package ac.dnd.server.admission.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import ac.dnd.server.admission.domain.AdmissionRepository;
import ac.dnd.server.admission.domain.Applicant;
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

	@Override
	public Optional<Applicant> findAdmissionByEventIdAndNameAndEmail(
		final Long eventId,
		final String name,
		final String email
	) {
		return applicantJpaRepository.findByEventIdAndNameAndEmail(
			eventId,
			name,
			email
		);
	}

}
