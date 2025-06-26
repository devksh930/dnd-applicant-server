package ac.dnd.server.admission.domain.model

import ac.dnd.server.common.support.EventStatus
import java.time.LocalDateTime

data class EventData(
    val id: Long,
    val name: String,
    val period: ViewablePeriod,
    val resultAnnouncementDateTime: LocalDateTime,
    val status: EventStatus
) {
    fun isCompleted(): Boolean {
        return status.isPublished()
    }

    fun isPending(): Boolean {
        return status.isPending()
    }

    fun isAnnounceable(currentTime: LocalDateTime): Boolean {
        return isPending() && resultAnnouncementDateTime.isAfter(currentTime)
    }
}