package ac.dnd.server.admission.domain.model

import ac.dnd.server.admission.exception.EventNotFoundException
import java.time.LocalDateTime

data class EventsData(
    val values: List<EventData>
) {
    fun findCurrentActiveEvent(now: LocalDateTime): EventData {
        return findCompletedEvent()
            ?: findNearestPendingEvent(now)
            ?: throw EventNotFoundException()
    }

    private fun findCompletedEvent(): EventData? {
        return values.firstOrNull { it.isCompleted() }
    }

    private fun findNearestPendingEvent(now: LocalDateTime): EventData? {
        return values
            .filter { it.isAnnounceable(now) }
            .minByOrNull { it.resultAnnouncementDateTime }
    }
}