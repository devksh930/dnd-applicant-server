package ac.dnd.server.admission.application.dto;

public record ApplicantStatusCheckCommand(
	Long eventId,
	String name,
	String email
) {
}
