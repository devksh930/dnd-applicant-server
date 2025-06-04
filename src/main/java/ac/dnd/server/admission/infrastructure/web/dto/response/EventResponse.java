package ac.dnd.server.admission.infrastructure.web.dto.response;

import ac.dnd.server.admission.domain.model.ViewablePeriod;

public record EventResponse(
	Long id,
	String name,
	ViewablePeriod period
) {

}
