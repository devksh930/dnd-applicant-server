package ac.dnd.server.admission.application.dto;

import java.time.LocalDateTime;

public record EventCreateCommand(
	String name,
	LocalDateTime startDateTime,
	LocalDateTime endDateTime
) {
}
