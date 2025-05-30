package ac.dnd.server.admission.infrastructure.web.dto.response;

import ac.dnd.server.enums.ApplicantStatus;

public record ApplicantCheckQueryResponse(
	String eventName,
	String name,
	ApplicantStatus status
) {
}
