package ac.dnd.server.admission.domain;

import java.time.LocalDateTime;

import ac.dnd.server.common.support.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {
	private String name;
	private ViewablePeriod period;

	public Event(
		final String name,
		final ViewablePeriod period
	) {
		this.name = name;
		this.period = period;
	}

	public boolean isViewable(
		final LocalDateTime now
	) {
		return period.contains(now);
	}
}
