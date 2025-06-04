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
			name,
			email,
			type,
			status
		);
	}

	public static Applicant create() {
		final String name = "홍길동";
		final String email = "test@test.com";

		final Applicant applicant = new Applicant(
			name,
			email,
			name,
			email,
			ApplicantType.BACKEND,
			ApplicantStatus.PASSED
		);

		applicant.withEvent(EventFixture.create());
		return applicant;
	}

	public static Applicant createWithStatus(ApplicantStatus status) {
		final String name = "홍길동";
		final String email = "test@test.com";
		return new Applicant(
			name,
			email,
			name,
			email,
			ApplicantType.BACKEND,
			status
		);
	}
}
