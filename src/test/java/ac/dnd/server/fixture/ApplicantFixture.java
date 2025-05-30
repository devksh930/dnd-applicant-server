package ac.dnd.server.fixture;

import ac.dnd.server.admission.infrastructure.persistence.entity.Applicant;
import ac.dnd.server.enums.ApplicantStatus;
import ac.dnd.server.enums.ApplicantType;

public class ApplicantFixture {

	public static Applicant create(
		final String name,
		final String email,
		final ApplicantType type,
		final ApplicantStatus status
	) {
		return new Applicant(
			name,
			email,
			type,
			status
		);
	}

	public static Applicant create() {
		final Applicant applicant = new Applicant(
			"홍길동",
			"test@test.com",
			ApplicantType.BACKEND,
			ApplicantStatus.PASSED
		);
		applicant.withEvent(EventFixture.create());
		return applicant;
	}

	public static Applicant createWithStatus(ApplicantStatus status) {
		return new Applicant(
			"홍길동",
			"test@test.com",
			ApplicantType.BACKEND,
			status
		);
	}
}
