package ac.dnd.server.admission.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ViewablePeriod {
	private LocalDateTime startDate;
	private LocalDateTime endDate;

	private ViewablePeriod(
		final LocalDateTime startDate,
		final LocalDateTime endDate
	) {
		if (startDate == null) {
			throw new IllegalArgumentException("시작일은 null일 수 없습니다.");
		}
		if (endDate == null) {
			throw new IllegalArgumentException("종료일은 null일 수 없습니다.");
		}

		if (!startDate.isBefore(endDate)) {
			throw new IllegalArgumentException("시작일(startDate)은 종료일(endDate)보다 이전이어야 합니다.");
		}

		this.startDate = startDate;
		this.endDate = endDate;
	}

	public static ViewablePeriod of(
		final LocalDateTime startDate,
		final LocalDateTime endDate
	) {
		return new ViewablePeriod(
			startDate,
			endDate
		);
	}

	public boolean contains(
		final LocalDateTime now
	) {
		return startDate.isBefore(now) && endDate.isAfter(now);
	}
}