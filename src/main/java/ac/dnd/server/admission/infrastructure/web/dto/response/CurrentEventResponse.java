package ac.dnd.server.admission.infrastructure.web.dto.response;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

public record CurrentEventResponse(
	Long id,
	String name,
	@DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm")
	LocalDateTime resultAnnouncementDateTime,
	Boolean isResultAnnounced
) {
}
