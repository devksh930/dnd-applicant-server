package ac.dnd.server.admission.infrastructure.persistence.entity;

import ac.dnd.server.admission.domain.model.ViewablePeriod;
import ac.dnd.server.common.support.BaseEntity;
import jakarta.persistence.Embedded;
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

	@Embedded
	private ViewablePeriod period;

	public Event(
		final String name,
		final ViewablePeriod period
	) {
		this.name = name;
		this.period = period;
	}

}
