package ac.dnd.server.admission.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import ac.dnd.server.admission.domain.model.ViewablePeriod;
import ac.dnd.server.common.support.BaseEntity;
import ac.dnd.server.common.support.EventStatus;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

	@Embedded
	private ViewablePeriod period;

	private LocalDateTime resultAnnouncementDateTime;

	@Enumerated(EnumType.STRING)
	private EventStatus status;

	public Event(
		final String name,
		final ViewablePeriod period,
		final LocalDateTime resultAnnouncementDateTime,
		final EventStatus status
	) {
		this.name = name;
		this.period = period;
		this.resultAnnouncementDateTime = resultAnnouncementDateTime;
		this.status = status;
	}

}
