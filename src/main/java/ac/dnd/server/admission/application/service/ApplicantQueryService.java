package ac.dnd.server.admission.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import ac.dnd.server.admission.application.dto.ApplicantStatusCheckCommand;
import ac.dnd.server.admission.domain.AdmissionRepository;
import ac.dnd.server.admission.domain.ApplicantValidator;
import ac.dnd.server.admission.domain.event.ApplicantQueryEvent;
import ac.dnd.server.admission.domain.model.ApplicantData;
import ac.dnd.server.admission.exception.ApplicantNotFoundException;
import ac.dnd.server.common.event.Events;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicantQueryService {
	private final AdmissionRepository admissionRepository;
	private final ApplicantValidator applicantValidator;

	public ApplicantData getApplicantStatusCheck(
		final ApplicantStatusCheckCommand command
	) {
		final ApplicantData applicant = admissionRepository.findAdmissionByEventIdAndNameAndEmail(
			command.eventId(),
			command.name(),
			command.email()
		).orElseThrow(ApplicantNotFoundException::new);

		applicantValidator.viewableValidator(
			applicant,
			LocalDateTime.now()
		);
		publishedApplicantEvent(applicant);

		return applicant;
	}

	private void publishedApplicantEvent(
		final ApplicantData applicant
	) {
		Events.raise(new ApplicantQueryEvent(
			applicant.id(),
			applicant.name(),
			applicant.email(),
			applicant.type(),
			applicant.status()
		));
	}
}
