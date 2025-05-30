package ac.dnd.server.admission.domain.model;

public record EventData(
	Long id,
	String name,
	ViewablePeriod period
) {

}
