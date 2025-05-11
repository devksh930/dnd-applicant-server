package ac.dnd.server.admission.ui;

import java.time.LocalDateTime;

public record EventCreateRequest(
	String name,
	LocalDateTime startDateTime,
	LocalDateTime endDateTime
) {
}
