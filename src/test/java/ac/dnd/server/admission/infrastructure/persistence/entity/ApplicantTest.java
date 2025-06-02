package ac.dnd.server.admission.infrastructure.persistence.entity;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ac.dnd.server.annotation.UnitTest;
import ac.dnd.server.enums.ApplicantStatus;
import ac.dnd.server.enums.ApplicantType;
import ac.dnd.server.fixture.ApplicantFixture;
import ac.dnd.server.fixture.EventFixture;

@UnitTest
class ApplicantTest {

	@Test
	void Applicant_객체를_생성하고_필드_값을_확인한다() {
		final String name = "홍길동";
		final String email = "gildong@example.com";
		final ApplicantType type = ApplicantType.FRONTEND;
		final ApplicantStatus status = ApplicantStatus.PASSED;

		final Applicant applicant = new Applicant(
			name,
			email,
			name,
			email,
			type,
			status
		);

		assertThat(applicant.getName()).isEqualTo(name);
		assertThat(applicant.getEmail()).isEqualTo(email);
		assertThat(applicant.getType()).isEqualTo(type);
		assertThat(applicant.getStatus()).isEqualTo(status);
		assertThat(applicant.getEvent()).isNull(); // 아직 Event가 설정되지 않음
	}

	@Test
	void withEvent_메서드로_Applicant에_Event를_설정한다() {
		final Applicant applicant = ApplicantFixture.create();
		final Event event = EventFixture.create();

		applicant.withEvent(event);

		Assertions.assertNotNull(applicant.getEvent());
		assertThat(applicant.getEvent()).isEqualTo(event);
	}
}