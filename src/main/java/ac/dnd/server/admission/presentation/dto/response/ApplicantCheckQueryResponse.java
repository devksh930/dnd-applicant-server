package ac.dnd.server.admission.presentation.dto.response;

import ac.dnd.server.enums.ApplicantStatus;

public record ApplicantCheckQueryResponse(
	String eventName,
	String name,
	ApplicantStatus status
) {
}
