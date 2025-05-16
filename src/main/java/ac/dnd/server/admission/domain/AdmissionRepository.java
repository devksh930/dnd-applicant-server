package ac.dnd.server.admission.domain;

import java.util.List;
import java.util.Optional;

public interface AdmissionRepository {
	Long saveEvent(final Event event);
	List<Event> getEvents();

	Optional<Applicant> findAdmissionByEventIdAndNameAndEmail(
		Long eventId,
		String name,
		String email
	);
}
