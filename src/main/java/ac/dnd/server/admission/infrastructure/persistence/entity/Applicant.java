package ac.dnd.server.admission.infrastructure.persistence.entity;

import ac.dnd.server.common.support.BaseEntity;
import ac.dnd.server.enums.ApplicantStatus;
import ac.dnd.server.enums.ApplicantType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "applicant")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Applicant extends BaseEntity {

	private String name;

	private String email;

	@Enumerated(EnumType.STRING)
	private ApplicantType type;

	@Enumerated(EnumType.STRING)
	private ApplicantStatus status;

	@ManyToOne(optional = false,
		fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	private Event event;

	public Applicant(
		final String name,
		final String email,
		final ApplicantType type,
		final ApplicantStatus status
	) {
		this.name = name;
		this.email = email;
		this.type = type;
		this.status = status;
	}

	public void withEvent(
		final Event event
	) {
		this.event = event;
	}

}
