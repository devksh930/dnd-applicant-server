package ac.dnd.server.fixture

import ac.dnd.server.admission.domain.model.EventData
import ac.dnd.server.admission.domain.model.ViewablePeriod
import ac.dnd.server.admission.domain.enums.EventStatus
import java.time.LocalDateTime

object EventDataFixture {
    
    fun create(): EventData {
        val now = LocalDateTime.now()
        return EventData(
            id = 1L,
            name = "테스트 이벤트",
            period = ViewablePeriodFixture.create(),
            resultAnnouncementDateTime = now,
            status = EventStatus.COMPLETED
        )
    }
    
    fun createCompleted(now: LocalDateTime = LocalDateTime.now()): EventData {
        return EventData(
            id = 1L,
            name = "발표 완료된 이벤트",
            period = ViewablePeriodFixture.createViewable(now),
            resultAnnouncementDateTime = now.minusDays(1),
            status = EventStatus.COMPLETED
        )
    }
    
    fun createPending(now: LocalDateTime = LocalDateTime.now()): EventData {
        return EventData(
            id = 2L,
            name = "발표 전인 이벤트",
            period = ViewablePeriodFixture.createViewable(now),
            resultAnnouncementDateTime = now.plusDays(1),
            status = EventStatus.PENDING
        )
    }
    
    fun createNearestPending(now: LocalDateTime = LocalDateTime.now()): EventData {
        return EventData(
            id = 1L,
            name = "가장 가까운 이벤트",
            period = ViewablePeriodFixture.createViewable(now),
            resultAnnouncementDateTime = now.plusHours(1),
            status = EventStatus.PENDING
        )
    }
    
    fun createFartherPending(now: LocalDateTime = LocalDateTime.now()): EventData {
        return EventData(
            id = 2L,
            name = "더 먼 이벤트",
            period = ViewablePeriodFixture.createViewable(now),
            resultAnnouncementDateTime = now.plusDays(5),
            status = EventStatus.PENDING
        )
    }
    
    fun createPastPending(now: LocalDateTime = LocalDateTime.now()): EventData {
        return EventData(
            id = 3L,
            name = "과거 이벤트",
            period = ViewablePeriodFixture.createViewable(now),
            resultAnnouncementDateTime = now.minusDays(1),
            status = EventStatus.PENDING
        )
    }
    
    fun createExpired(now: LocalDateTime = LocalDateTime.now()): EventData {
        return EventData(
            id = 2L,
            name = "만료된 이벤트",
            period = ViewablePeriodFixture.createViewable(now),
            resultAnnouncementDateTime = now.minusDays(5),
            status = EventStatus.EXPIRED
        )
    }
    
    fun createWithCustomValues(
        id: Long = 1L,
        name: String = "커스텀 이벤트",
        period: ViewablePeriod = ViewablePeriodFixture.create(),
        resultAnnouncementDateTime: LocalDateTime = LocalDateTime.now(),
        status: EventStatus = EventStatus.COMPLETED
    ): EventData {
        return EventData(id, name, period, resultAnnouncementDateTime, status)
    }
}