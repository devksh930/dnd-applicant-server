package ac.dnd.server.admission.application.dto;

import ac.dnd.server.admission.domain.ViewablePeriod;

public record EventData(
	Long id,
	String name,
	ViewablePeriod period
) {

}
