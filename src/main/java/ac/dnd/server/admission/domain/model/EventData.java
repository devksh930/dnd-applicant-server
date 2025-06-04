package ac.dnd.server.admission.domain.model;

import java.time.LocalDateTime;

import ac.dnd.server.common.support.EventStatus;

public record EventData(
	Long id,
	String name,
	ViewablePeriod period,
	LocalDateTime resultAnnouncementDateTime,
	EventStatus status
) {
	public boolean isCompleted() {
		return status.isPublished();
	}

	public boolean isPending() {
		return status.isPending();
	}

	public boolean isAnnounceable(final LocalDateTime currentTime) {
		return isPending() && resultAnnouncementDateTime.isAfter(currentTime);
	}

}
