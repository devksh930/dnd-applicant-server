package ac.dnd.server.admission.presentation.dto.response;

import ac.dnd.server.admission.domain.ViewablePeriod;

public record EventResponse(
	Long id,
	String name,
	ViewablePeriod period
) {

}
