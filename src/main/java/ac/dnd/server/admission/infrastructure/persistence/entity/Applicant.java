package ac.dnd.server.admission.infrastructure.persistence.entity;

import ac.dnd.server.admission.infrastructure.persistence.converter.StringCryptoConverter;
import ac.dnd.server.admission.infrastructure.persistence.converter.StringHmacConverter;
import ac.dnd.server.common.support.BaseEntity;
import ac.dnd.server.enums.ApplicantStatus;
import ac.dnd.server.enums.ApplicantType;
import jakarta.persistence.Convert;
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

	@Convert(converter = StringCryptoConverter.class)
	private String name;

	@Convert(converter = StringCryptoConverter.class)
	private String email;

	@Convert(converter = StringHmacConverter.class)
	private String nameBlindIndex;

	@Convert(converter = StringHmacConverter.class)
	private String emailBlindIndex;

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
		final String nameBlindIndex,
		final String emailBlindIndex,
		final ApplicantType type,
		final ApplicantStatus status
	) {
		this.name = name;
		this.email = email;
		this.nameBlindIndex = nameBlindIndex;
		this.emailBlindIndex = emailBlindIndex;
		this.type = type;
		this.status = status;
	}

	public void withEvent(
		final Event event
	) {
		this.event = event;
	}

}
