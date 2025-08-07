package ac.dnd.server.admission.infrastructure.web.dto.response

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

data class CurrentEventResponse(
    val id: Long,
    val name: String,
    @field:DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm")
    val resultAnnouncementDateTime: LocalDateTime,
    val isResultAnnounced: Boolean
)