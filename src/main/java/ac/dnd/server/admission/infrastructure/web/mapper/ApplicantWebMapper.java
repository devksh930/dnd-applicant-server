package ac.dnd.server.admission.infrastructure.web.mapper;

import org.springframework.stereotype.Component;

import ac.dnd.server.admission.application.dto.ApplicantStatusCheckCommand;
import ac.dnd.server.admission.infrastructure.web.dto.request.ApplicantStatusCheckRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicantWebMapper {

	public ApplicantStatusCheckCommand statusCheckRequestToCommand(
		ApplicantStatusCheckRequest request,
		final Long eventId
	) {
		return new ApplicantStatusCheckCommand(
			eventId,
			request.name(),
			request.email()

		);
	}

}
