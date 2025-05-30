package ac.dnd.server.admission.infrastructure.persistence.mapper;

import static ac.dnd.server.fixture.TextEncryptorFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import ac.dnd.server.admission.domain.model.ApplicantData;
import ac.dnd.server.admission.domain.model.EventData;
import ac.dnd.server.admission.domain.model.ViewablePeriod;
import ac.dnd.server.admission.infrastructure.persistence.entity.Applicant;
import ac.dnd.server.admission.infrastructure.persistence.entity.Event;
import ac.dnd.server.annotation.UnitTest;
import ac.dnd.server.enums.ApplicantStatus;
import ac.dnd.server.enums.ApplicantType;
import ac.dnd.server.fixture.EventFixture;

@UnitTest
public class ApplicantPersistenceMapperTest {
	private ApplicantPersistenceMapper applicantPersistenceMapper = new ApplicantPersistenceMapper(createNoOpTextEncryptor());

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

		final ApplicantData applicantData = applicantPersistenceMapper.applicantEntityToDomain(entity);

		// then
		assertThat(applicantData.name()).isEqualTo(name);
		assertThat(applicantData.email()).isEqualTo(email);
		assertThat(applicantData.type()).isEqualTo(type);
		assertThat(applicantData.status()).isEqualTo(status);
	}

	@Test
	void 이벤트_도메인에서_엔티티로_매핑한다() {
		// given
		String name = "테스트 이벤트";
		ViewablePeriod period = ViewablePeriod.of(
			LocalDateTime.now(),
			LocalDateTime.now().plusDays(1)
		);
		EventData domain = new EventData(
			1L,
			name,
			period
		);

		// when
		Event entity = applicantPersistenceMapper.eventDataToEntity(domain);

		// then
		assertThat(entity.getName()).isEqualTo(name);
		assertThat(entity.getPeriod()).isEqualTo(period);
	}

	@Test
	void 이벤트_엔티티에서_도메인으로_매핑한다() {
		// given
		Long id = 1L;
		String name = "테스트 이벤트";
		ViewablePeriod period = ViewablePeriod.of(
			LocalDateTime.now(),
			LocalDateTime.now().plusDays(1)
		);
		Event entity = new Event(
			name,
			period
		);
		ReflectionTestUtils.setField(
			entity,
			"id",
			id
		);

		// when
		EventData domain = applicantPersistenceMapper.eventEntityToDomain(entity);

		// then
		assertThat(domain.id()).isEqualTo(id);
		assertThat(domain.name()).isEqualTo(name);
		assertThat(domain.period()).isEqualTo(period);
	}
}
