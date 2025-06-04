package ac.dnd.server.admission.domain.model;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import ac.dnd.server.admission.exception.EventNotFoundException;
import ac.dnd.server.annotation.UnitTest;
import ac.dnd.server.common.support.EventStatus;

@UnitTest
class EventsDataTest {

	@Test
	void 완료된_이벤트가_존재하면_해당_이벤트를_반환한다() {
		// given
		final LocalDateTime now = LocalDateTime.now();
		final ViewablePeriod period = ViewablePeriod.of(
			now.minusDays(10),
			now.plusDays(10)
		);

		final EventData completedEvent = new EventData(
			1L,
			"발표 완료된 이벤트",
			period,
			now.minusDays(1),
			EventStatus.COMPLETED
		);

		final EventData pendingEvent = new EventData(
			2L,
			"발표 전인 이벤트",
			period,
			now.plusDays(1),
			EventStatus.PENDING
		);

		final EventsData eventsData = new EventsData(List.of(
			completedEvent,
			pendingEvent
		));

		// when
		final EventData currentEvent = eventsData.findCurrentActiveEvent(now);

		// then
		assertThat(currentEvent).isEqualTo(completedEvent);
		assertThat(currentEvent.status()).isEqualTo(EventStatus.COMPLETED);
	}

	@Test
	void 완료된_이벤트는_없고_미래의_진행_예정_이벤트만_있으면_가장_가까운_이벤트를_반환한다() {
		// given
		final LocalDateTime now = LocalDateTime.now();
		final ViewablePeriod period = ViewablePeriod.of(
			now.minusDays(10),
			now.plusDays(10)
		);

		// 다양한 PENDING 상태의 이벤트들
		final EventData nearestPendingEvent = new EventData(
			1L,
			"가장 가까운 이벤트",
			period,
			now.plusHours(1),
			EventStatus.PENDING
		);
		final EventData fartherPendingEvent = new EventData(
			2L,
			"더 먼 이벤트",
			period,
			now.plusDays(5),
			EventStatus.PENDING
		);
		final EventData pastPendingEvent = new EventData(
			3L,
			"과거 이벤트",
			period,
			now.minusDays(1),
			EventStatus.PENDING
		); // 이 이벤트는 무시되어야 함

		final EventsData eventsData = new EventsData(List.of(
			fartherPendingEvent,
			nearestPendingEvent,
			pastPendingEvent
		));

		// when
		final EventData currentEvent = eventsData.findCurrentActiveEvent(now);

		// then
		assertThat(currentEvent).isEqualTo(nearestPendingEvent);
	}

	@Test
	void 활성화된_이벤트가_없으면_예외가_발생한다() {
		// given
		final LocalDateTime now = LocalDateTime.now();
		final ViewablePeriod period = ViewablePeriod.of(
			now.minusDays(10),
			now.plusDays(10)
		);

		// 유효한 이벤트가 없는 경우 (과거 PENDING, EXPIRED)
		final EventData pastPendingEvent = new EventData(
			1L,
			"과거 PENDING 이벤트",
			period,
			now.minusDays(2),
			EventStatus.PENDING
		);
		final EventData expiredEvent = new EventData(
			2L,
			"만료된 이벤트",
			period,
			now.minusDays(5),
			EventStatus.EXPIRED
		);

		final EventsData eventsData = new EventsData(List.of(
			pastPendingEvent,
			expiredEvent
		));

		// when & then
		assertThrows(
			EventNotFoundException.class,
			() -> eventsData.findCurrentActiveEvent(LocalDateTime.now())
		);

	}

	@Test
	void 이벤트_목록이_비어있으면_예외가_발생한다() {
		// given
		final EventsData eventsData = new EventsData(List.of());

		// when & then
		assertThrows(
			EventNotFoundException.class,
			() -> eventsData.findCurrentActiveEvent(LocalDateTime.now())
		);
	}
}