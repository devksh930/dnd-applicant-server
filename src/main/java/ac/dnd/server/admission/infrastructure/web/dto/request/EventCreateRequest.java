package ac.dnd.server.admission.infrastructure.web.dto.request;

import java.time.LocalDateTime;

import ac.dnd.server.common.annotation.StartBeforeEnd;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@StartBeforeEnd(startDateTime = "startDateTime", endDateTime = "endDateTime")
public record EventCreateRequest(
	@NotBlank
	String name,
	@NotNull
	LocalDateTime startDateTime,
	@NotNull
	LocalDateTime endDateTime
) {
}
