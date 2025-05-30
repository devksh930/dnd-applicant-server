package ac.dnd.server.admission.infrastructure.web.mapper;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import ac.dnd.server.admission.application.dto.ApplicantStatusCheckCommand;
import ac.dnd.server.admission.infrastructure.web.dto.request.ApplicantStatusCheckRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicantWebMapper {
	private final TextEncryptor encryptor;

	public ApplicantStatusCheckCommand statusCheckRequestToCommand(
		ApplicantStatusCheckRequest request,
		final Long eventId
	) {
		return new ApplicantStatusCheckCommand(
			eventId,
			encryptor.encrypt(request.name()),
			encryptor.encrypt(request.email()
			)
		);
	}

}
