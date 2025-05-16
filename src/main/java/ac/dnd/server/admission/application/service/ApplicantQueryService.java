package ac.dnd.server.admission.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import ac.dnd.server.admission.application.dto.ApplicantData;
import ac.dnd.server.admission.application.dto.ApplicantStatusCheckCommand;
import ac.dnd.server.admission.application.mapper.ApplicantMapper;
import ac.dnd.server.admission.domain.AdmissionRepository;
import ac.dnd.server.admission.domain.Applicant;
import ac.dnd.server.admission.domain.ApplicantValidator;
import ac.dnd.server.admission.exception.ApplicantNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicantQueryService {
	private final AdmissionRepository admissionRepository;
	private final ApplicantValidator applicantValidator;
	private final ApplicantMapper applicantMapper;

	public ApplicantData getApplicantStatusCheck(
		final ApplicantStatusCheckCommand command
	) {
		final Applicant applicant = admissionRepository.findAdmissionByEventIdAndNameAndEmail(
			command.eventId(),
			command.name(),
			command.email()
		).orElseThrow(ApplicantNotFoundException::new);

		applicantValidator.viewableValidator(
			applicant,
			LocalDateTime.now()
		);

		return applicantMapper.entityToData(applicant);
	}
}
