package ac.dnd.server.fixture;

import java.time.LocalDateTime;

import ac.dnd.server.admission.domain.Event;
import ac.dnd.server.admission.domain.ViewablePeriod;

public class EventFixture {

	public static Event create() {
		return new Event(
			"픽스처이벤트",
			ViewablePeriod.of(
				LocalDateTime.now().minusHours(1),
				LocalDateTime.now().plusHours(1)
			)
		);
	}

	public static Event viewabelEvent(
		final LocalDateTime now
	) {
		final ViewablePeriod viewablePeriod = ViewablePeriod.of(
			now.minusHours(1),
			now.plusHours(1)
		);
		return new Event(
			"조회 가능 이벤트",
			viewablePeriod
		);
	}

	public static Event notViewableEventBefore(
		final LocalDateTime now
	) {
		ViewablePeriod notViewablePeriodBefore = ViewablePeriod.of(
			now.plusHours(1),
			now.plusHours(2)
		);
		return new Event(
			"조회 불가능 이벤트 (시작 전)",
			notViewablePeriodBefore
		);
	}

	public static Event notViewableEventAfter(
		final LocalDateTime now
	) {
		ViewablePeriod notViewablePeriodAfter = ViewablePeriod.of(
			now.minusHours(2),
			now.minusHours(1)
		);
		return new Event(
			"조회 불가능 이벤트 (종료 후)",
			notViewablePeriodAfter
		);
	}

}
