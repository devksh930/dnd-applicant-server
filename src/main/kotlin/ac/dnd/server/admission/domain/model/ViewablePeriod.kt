package ac.dnd.server.admission.domain.model

import jakarta.persistence.Embeddable
import java.time.LocalDateTime

@Embeddable
data class ViewablePeriod (
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
) {
    init {
        require(!startDate.isAfter(endDate)) {
            "시작일(startDate)은 종료일(endDate)보다 이전이어야 합니다."
        }
    }

    companion object {
        fun of(startDate: LocalDateTime, endDate: LocalDateTime): ViewablePeriod {
            return ViewablePeriod(startDate, endDate)
        }
    }

    fun contains(now: LocalDateTime): Boolean {
        return startDate.isBefore(now) && endDate.isAfter(now)
    }
}