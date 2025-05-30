package ac.dnd.server.admission.domain.event;

import ac.dnd.server.enums.ApplicantStatus;
import ac.dnd.server.enums.ApplicantType;

public record ApplicantQueryEvent(
	Long applicantId,
	String name,
	String email,
	ApplicantType type,
	ApplicantStatus status
) {
}
