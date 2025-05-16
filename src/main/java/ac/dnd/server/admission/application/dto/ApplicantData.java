package ac.dnd.server.admission.application.dto;

import ac.dnd.server.enums.ApplicantStatus;
import ac.dnd.server.enums.ApplicantType;

public record ApplicantData(
	String eventName,
	String name,
	String email,
	ApplicantType type,
	ApplicantStatus status
) {
}
