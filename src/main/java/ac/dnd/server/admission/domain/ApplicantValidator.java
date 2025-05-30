package ac.dnd.server.admission.domain;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import ac.dnd.server.admission.domain.model.ApplicantData;
import ac.dnd.server.admission.exception.ApplicantViewablePeriodEndException;

@Component
public class ApplicantValidator {

	public void viewableValidator(
		final ApplicantData applicant,
		final LocalDateTime now
	) {
		if (!applicant.isViewable(now)) {
			throw new ApplicantViewablePeriodEndException();
		}
	}
}
