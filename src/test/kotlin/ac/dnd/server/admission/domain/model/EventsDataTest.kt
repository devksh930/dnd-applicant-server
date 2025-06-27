package ac.dnd.server.admission.domain.model

import ac.dnd.server.admission.exception.EventNotFoundException
import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.enums.EventStatus
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

@UnitTest
class EventsDataTest : DescribeSpec({

    describe("EventsData") {

        it("완료된 이벤트가 존재하면 해당 이벤트를 반환한다") {
            // given
            val now = LocalDateTime.now()
            val period = ViewablePeriod.of(
                now.minusDays(10),
                now.plusDays(10)
            )

            val completedEvent = EventData(
                1L,
                "발표 완료된 이벤트",
                period,
                now.minusDays(1),
                EventStatus.COMPLETED
            )

            val pendingEvent = EventData(
                2L,
                "발표 전인 이벤트",
                period,
                now.plusDays(1),
                EventStatus.PENDING
            )

            val eventsData = EventsData(listOf(
                completedEvent,
                pendingEvent
            ))

            // when
            val currentEvent = eventsData.findCurrentActiveEvent(now)

            // then
            currentEvent shouldBe completedEvent
            currentEvent.status shouldBe EventStatus.COMPLETED
        }

        it("완료된 이벤트는 없고 미래의 진행 예정 이벤트만 있으면 가장 가까운 이벤트를 반환한다") {
            // given
            val now = LocalDateTime.now()
            val period = ViewablePeriod.of(
                now.minusDays(10),
                now.plusDays(10)
            )

            // 다양한 PENDING 상태의 이벤트들
            val nearestPendingEvent = EventData(
                1L,
                "가장 가까운 이벤트",
                period,
                now.plusHours(1),
                EventStatus.PENDING
            )
            val fartherPendingEvent = EventData(
                2L,
                "더 먼 이벤트",
                period,
                now.plusDays(5),
                EventStatus.PENDING
            )
            val pastPendingEvent = EventData(
                3L,
                "과거 이벤트",
                period,
                now.minusDays(1),
                EventStatus.PENDING
            ) // 이 이벤트는 무시되어야 함

            val eventsData = EventsData(listOf(
                fartherPendingEvent,
                nearestPendingEvent,
                pastPendingEvent
            ))

            // when
            val currentEvent = eventsData.findCurrentActiveEvent(now)

            // then
            currentEvent shouldBe nearestPendingEvent
        }

        it("활성화된 이벤트가 없으면 예외가 발생한다") {
            // given
            val now = LocalDateTime.now()
            val period = ViewablePeriod.of(
                now.minusDays(10),
                now.plusDays(10)
            )

            // 유효한 이벤트가 없는 경우 (과거 PENDING, EXPIRED)
            val pastPendingEvent = EventData(
                1L,
                "과거 PENDING 이벤트",
                period,
                now.minusDays(2),
                EventStatus.PENDING
            )
            val expiredEvent = EventData(
                2L,
                "만료된 이벤트",
                period,
                now.minusDays(5),
                EventStatus.EXPIRED
            )

            val eventsData = EventsData(listOf(
                pastPendingEvent,
                expiredEvent
            ))

            // when & then
            shouldThrow<EventNotFoundException> {
                eventsData.findCurrentActiveEvent(LocalDateTime.now())
            }
        }

        it("이벤트 목록이 비어있으면 예외가 발생한다") {
            // given
            val eventsData = EventsData(emptyList())

            // when & then
            shouldThrow<EventNotFoundException> {
                eventsData.findCurrentActiveEvent(LocalDateTime.now())
            }
        }
    }
})