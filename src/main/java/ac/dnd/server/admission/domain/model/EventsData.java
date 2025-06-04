package ac.dnd.server.admission.domain.model;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import ac.dnd.server.admission.exception.EventNotFoundException;

public record EventsData(
	List<EventData> values
) {

	public EventData findCurrentActiveEvent(
		final LocalDateTime now
	) {
		return findCompletedEvent()
			.orElseGet(() -> findNearestPendingEvent(now)
				.orElseThrow(EventNotFoundException::new));
	}

	private Optional<EventData> findCompletedEvent() {
		return this.values.stream()
			.filter(EventData::isCompleted)
			.findFirst();
	}

	private Optional<EventData> findNearestPendingEvent(final LocalDateTime now) {
		return this.values.stream()
			.filter(event -> event.isAnnounceable(now))
			.min(Comparator.comparing(EventData::resultAnnouncementDateTime));
	}
}