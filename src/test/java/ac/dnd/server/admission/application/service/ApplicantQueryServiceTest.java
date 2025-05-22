package ac.dnd.server.admission.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import ac.dnd.server.admission.application.dto.ApplicantData;
import ac.dnd.server.admission.application.dto.ApplicantStatusCheckCommand;
import ac.dnd.server.admission.application.mapper.ApplicantMapper;
import ac.dnd.server.admission.domain.AdmissionRepository;
import ac.dnd.server.admission.domain.Applicant;
import ac.dnd.server.admission.domain.ApplicantValidator;
import ac.dnd.server.admission.domain.event.ApplicantQueryEvent;
import ac.dnd.server.admission.exception.ApplicantNotFoundException;
import ac.dnd.server.common.event.Events;
import ac.dnd.server.fixture.ApplicantFixture;

@ExtendWith(MockitoExtension.class)
class ApplicantQueryServiceTest {
	@Mock
	AdmissionRepository admissionRepository;
	@Mock
	ApplicantValidator applicantValidator;
	@Mock
	ApplicantMapper applicantMapper;

	@InjectMocks
	ApplicantQueryService applicantQueryService;

	@Test
	void 정상적으로_합격여부를_조회한다() {
		final Applicant fixture = ApplicantFixture.create();
		when(admissionRepository.findAdmissionByEventIdAndNameAndEmail(
			anyLong(),
			anyString(),
			anyString()
		)).thenReturn(Optional.of(fixture));

		when(applicantMapper.entityToData(
			fixture
		)).thenReturn(new ApplicantData(
			fixture.getEvent().getName(),
			fixture.getName(),
			fixture.getEmail(),
			fixture.getType(),
			fixture.getStatus()
		));

		ApplicantData result = applicantQueryService.getApplicantStatusCheck(new ApplicantStatusCheckCommand(
			1L,
			"name",
			"email"
		));
		Assertions.assertEquals(
			new ApplicantData(
				fixture.getEvent().getName(),
				fixture.getName(),
				fixture.getEmail(),
				fixture.getType(),
				fixture.getStatus()
			),
			result
		);
	}

	@Test
	void 존재하지않는_참가자일경우_NotFound예외를_던진다() {
		when(admissionRepository.findAdmissionByEventIdAndNameAndEmail(
			anyLong(),
			anyString(),
			anyString()
		)).thenReturn(Optional.empty());

		assertThrows(
			ApplicantNotFoundException.class,
			() -> applicantQueryService.getApplicantStatusCheck(new ApplicantStatusCheckCommand(
				anyLong(),
				anyString(),
				anyString()
			))
		);

	}

	@Test
	void 합격자조회_이벤트가_정상적으로_발행된다() {
		try (MockedStatic<Events> eventsMock = mockStatic(Events.class)) {
			final Applicant fixture = ApplicantFixture.create();
			when(admissionRepository.findAdmissionByEventIdAndNameAndEmail(
				anyLong(),
				anyString(),
				anyString()
			)).thenReturn(Optional.of(fixture));

			when(applicantMapper.entityToData(
				fixture
			)).thenReturn(new ApplicantData(
				fixture.getEvent().getName(),
				fixture.getName(),
				fixture.getEmail(),
				fixture.getType(),
				fixture.getStatus()
			));

			applicantQueryService.getApplicantStatusCheck(new ApplicantStatusCheckCommand(
				1L,
				"name",
				"email"
			));

			eventsMock.verify(
				() -> Events.raise(any(ApplicantQueryEvent.class)),
				times(1)
			);
		}
	}

}
