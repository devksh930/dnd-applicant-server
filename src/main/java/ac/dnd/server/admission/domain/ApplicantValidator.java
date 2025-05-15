package ac.dnd.server.admission.domain;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import ac.dnd.server.admission.exception.ApplicantViewablePeriodEndException;

@Component
public class ApplicantValidator {

	public void viewableValidator(
		final Applicant applicant,
		final LocalDateTime now
	) {
		if (!applicant.isAdmissionResultViewable(now)) {
			throw new ApplicantViewablePeriodEndException();
		}
	}
}
