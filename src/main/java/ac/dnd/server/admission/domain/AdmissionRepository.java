package ac.dnd.server.admission.domain;

import java.util.List;

public interface AdmissionRepository {
	Long saveEvent(final Event event);
	List<Event> getEvents();
}
