package ac.dnd.server.admission.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import ac.dnd.server.admission.domain.model.ApplicantData;
import ac.dnd.server.admission.domain.model.EventData;
import ac.dnd.server.admission.infrastructure.persistence.entity.Applicant;
import ac.dnd.server.admission.infrastructure.persistence.entity.Event;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicantPersistenceMapper {

	public Event eventDataToEntity(
		final EventData domain
	) {
		return new Event(
			domain.name(),
			domain.period()
		);
	}

	public EventData eventEntityToDomain(final Event entity) {
		return new EventData(
			entity.getId(),
			entity.getName(),
			entity.getPeriod()
		);
	}

	public ApplicantData applicantEntityToDomain(
		final Applicant entity
	) {
		return new ApplicantData(
			entity.getId(),
			entity.getEvent().getName(),
			entity.getName(),
			entity.getEmail(),
			entity.getType(),
			entity.getStatus(),
			entity.getEvent().getPeriod()
		);
	}
}
