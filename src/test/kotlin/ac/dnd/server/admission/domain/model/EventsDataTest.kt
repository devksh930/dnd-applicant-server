package ac.dnd.server.admission.domain.model

import ac.dnd.server.admission.exception.EventNotFoundException
import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.admission.domain.enums.EventStatus
import ac.dnd.server.fixture.EventDataFixture
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
            val completedEvent = EventDataFixture.createCompleted(now)
            val pendingEvent = EventDataFixture.createPending(now)

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
            val nearestPendingEvent = EventDataFixture.createNearestPending(now)
            val fartherPendingEvent = EventDataFixture.createFartherPending(now)
            val pastPendingEvent = EventDataFixture.createPastPending(now) // 이 이벤트는 무시되어야 함

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
            val pastPendingEvent = EventDataFixture.createPastPending(now)
            val expiredEvent = EventDataFixture.createExpired(now)

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
