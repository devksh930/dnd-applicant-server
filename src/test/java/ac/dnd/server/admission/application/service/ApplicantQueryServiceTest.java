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

import ac.dnd.server.admission.application.dto.ApplicantStatusCheckCommand;
import ac.dnd.server.admission.domain.AdmissionRepository;
import ac.dnd.server.admission.domain.ApplicantValidator;
import ac.dnd.server.admission.domain.event.ApplicantQueryEvent;
import ac.dnd.server.admission.domain.model.ApplicantData;
import ac.dnd.server.admission.exception.ApplicantNotFoundException;
import ac.dnd.server.common.event.Events;
import ac.dnd.server.fixture.ApplicantDataFixture;

@ExtendWith(MockitoExtension.class)
class ApplicantQueryServiceTest {
	@Mock
	AdmissionRepository admissionRepository;
	@Mock
	ApplicantValidator applicantValidator;

	@InjectMocks
	ApplicantQueryService applicantQueryService;

	@Test
	void 정상적으로_합격여부를_조회한다() {

		final ApplicantData fixture = ApplicantDataFixture.create();
		when(admissionRepository.findAdmissionByEventIdAndNameAndEmail(
			anyLong(),
			anyString(),
			anyString()
		)).thenReturn(Optional.of(fixture));

		ApplicantData result = applicantQueryService.getApplicantStatusCheck(
			new ApplicantStatusCheckCommand(
				1L,
				"name",
				"email"
			));

		Assertions.assertEquals(
			fixture,
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
			final ApplicantData fixture = ApplicantDataFixture.create();
			when(admissionRepository.findAdmissionByEventIdAndNameAndEmail(
				anyLong(),
				anyString(),
				anyString()
			)).thenReturn(Optional.of(fixture));


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
