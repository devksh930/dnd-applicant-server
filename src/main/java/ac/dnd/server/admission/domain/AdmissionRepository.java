package ac.dnd.server.admission.domain;

public interface AdmissionRepository {
	Long saveEvent(final Event event);
}
