package ac.dnd.server.admission.domain;

import java.util.List;
import java.util.Optional;

import ac.dnd.server.admission.domain.model.ApplicantData;
import ac.dnd.server.admission.domain.model.EventData;

public interface AdmissionRepository {
	Long saveEvent(final EventData event);

	List<EventData> getEvents();

	Optional<ApplicantData> findAdmissionByEventIdAndNameAndEmail(
		Long eventId,
		String name,
		String email
	);
}
