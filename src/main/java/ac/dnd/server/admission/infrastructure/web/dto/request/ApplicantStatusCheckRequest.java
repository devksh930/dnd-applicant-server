package ac.dnd.server.admission.infrastructure.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ApplicantStatusCheckRequest(
	@NotBlank
	String name,
	@Email
	@NotBlank
	String email
) {
}
