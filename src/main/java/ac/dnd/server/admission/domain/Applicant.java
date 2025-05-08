package ac.dnd.server.admission.domain;

import ac.dnd.server.common.BaseEntity;
import ac.dnd.server.enums.ApplicantStatus;
import ac.dnd.server.enums.ApplicantType;
import jakarta.persistence.Entity;
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
	private ApplicantType type;
	private ApplicantStatus status;

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
}
