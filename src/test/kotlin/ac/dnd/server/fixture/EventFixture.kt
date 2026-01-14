package ac.dnd.server.fixture

import ac.dnd.server.admission.domain.model.ViewablePeriod
import ac.dnd.server.admission.infrastructure.persistence.entity.EventEntity
import ac.dnd.server.admission.domain.enums.EventStatus
import java.time.LocalDateTime

object EventFixture {

    fun create(): EventEntity {
        return EventEntity(
            "픽스처이벤트",
            ViewablePeriod.of(
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1)
            ),
            LocalDateTime.now(),
            EventStatus.COMPLETED
        )
    }

    fun viewableEvent(now: LocalDateTime): EventEntity {
        val viewablePeriod = ViewablePeriod.of(
            now.minusHours(1),
            now.plusHours(1)
        )
        return EventEntity(
            "조회 가능 이벤트",
            viewablePeriod,
            LocalDateTime.now(),
            EventStatus.COMPLETED
        )
    }

    fun notViewableEventBefore(now: LocalDateTime): EventEntity {
        val notViewablePeriodBefore = ViewablePeriod.of(
            now.plusHours(1),
            now.plusHours(2)
        )
        return EventEntity(
            "조회 불가능 이벤트 (시작 전)",
            notViewablePeriodBefore,
            LocalDateTime.MIN,
            EventStatus.EXPIRED
        )
    }

    fun notViewableEventAfter(now: LocalDateTime): EventEntity {
        val notViewablePeriodAfter = ViewablePeriod.of(
            now.minusHours(2),
            now.minusHours(1)
        )
        return EventEntity(
            "조회 불가능 이벤트 (종료 후)",
            notViewablePeriodAfter,
            LocalDateTime.MIN,
            EventStatus.EXPIRED
        )
    }

    fun createForMapperTest(
        name: String = "테스트 이벤트",
        period: ViewablePeriod = ViewablePeriod.of(LocalDateTime.now(), LocalDateTime.now().plusDays(1)),
        resultAnnouncementDateTime: LocalDateTime = LocalDateTime.now(),
        status: EventStatus = EventStatus.COMPLETED
    ): EventEntity {
        return EventEntity(
            name,
            period,
            resultAnnouncementDateTime,
            status
        )
    }
}
