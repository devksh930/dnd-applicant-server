package ac.dnd.server.admission.application.mapper;

import org.springframework.stereotype.Component;

import ac.dnd.server.admission.application.dto.ApplicantData;
import ac.dnd.server.admission.domain.Applicant;

@Component
public class ApplicantMapper {

	public ApplicantData entityToData(
		final Applicant entity
	) {
		return new ApplicantData(
			entity.getEvent().getName(),
			entity.getName(),
			entity.getEmail(),
			entity.getType(),
			entity.getStatus()
		);
	}
}
