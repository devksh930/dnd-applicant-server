package ac.dnd.server.admission.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ApplicatStatusCheckRequest(
	@NotBlank
	String name,
	@Email
	@NotBlank
	String email
) {
}
