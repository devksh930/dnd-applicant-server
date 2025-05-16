package ac.dnd.server.admission.application.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.Test;

import ac.dnd.server.admission.application.dto.ApplicantData;
import ac.dnd.server.admission.domain.Applicant;
import ac.dnd.server.admission.domain.Event;
import ac.dnd.server.annotation.UnitTest;
import ac.dnd.server.enums.ApplicantStatus;
import ac.dnd.server.enums.ApplicantType;
import ac.dnd.server.fixture.EventFixture;

@UnitTest
public class ApplicantMapperTest {
	private ApplicantMapper applicantMapper = new ApplicantMapper();

	@Test
	void 모든_필드가_채워진_엔티티를_정상적으로_매핑한다() {
		// given
		String name = "테스트";
		String email = "test@test.com";
		final ApplicantType type = ApplicantType.BACKEND;
		final ApplicantStatus status = ApplicantStatus.PASSED;
		final Event event = EventFixture.create();
		Applicant entity = new Applicant(
			name,
			email,
			type,
			status
		);

		entity.withEvent(event);

		final ApplicantData applicantData = applicantMapper.entityToData(entity);

		// then
		assertThat(applicantData.name()).isEqualTo(name);
		assertThat(applicantData.email()).isEqualTo(email);
		assertThat(applicantData.type()).isEqualTo(type);
		assertThat(applicantData.status()).isEqualTo(status);
	}
}
