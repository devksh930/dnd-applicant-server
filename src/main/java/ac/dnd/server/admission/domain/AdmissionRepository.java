package ac.dnd.server.admission.domain;

import java.util.List;
import java.util.Optional;

import ac.dnd.server.admission.domain.model.ApplicantData;
import ac.dnd.server.admission.domain.model.EventData;
import ac.dnd.server.admission.domain.model.EventsData;
import ac.dnd.server.common.support.EventStatus;

public interface AdmissionRepository {
	Long saveEvent(final EventData event);

	List<EventData> getEvents();

	Optional<ApplicantData> findAdmissionByEventIdAndNameAndEmail(
		Long eventId,
		String name,
		String email
	);

	EventsData findByStatusIn(List<EventStatus> statuses);

}
