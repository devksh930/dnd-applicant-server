package ac.dnd.server.admission.domain.model;

import java.time.LocalDateTime;

import ac.dnd.server.enums.ApplicantStatus;
import ac.dnd.server.enums.ApplicantType;

public record ApplicantData(
	Long id,
	String eventName,
	String name,
	String email,
	ApplicantType type,
	ApplicantStatus status,
	ViewablePeriod period
) {

	public boolean isViewable(
		final LocalDateTime now
	) {
		return period.contains(now);
	}
}
