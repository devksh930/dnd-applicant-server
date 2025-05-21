package ac.dnd.server.admission.application.mapper;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import ac.dnd.server.admission.application.dto.ApplicantData;
import ac.dnd.server.admission.domain.Applicant;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicantMapper {
	private final TextEncryptor textEncryptor;

	public ApplicantData entityToData(
		final Applicant entity
	) {
		return new ApplicantData(
			entity.getEvent().getName(),
			textEncryptor.decrypt(entity.getName()),
			textEncryptor.decrypt(entity.getEmail()),
			entity.getType(),
			entity.getStatus()
		);
	}
}
