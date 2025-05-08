package ac.dnd.server.admission.domain;

import java.time.LocalDateTime;
import java.util.Objects;

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
		this.startDate = Objects.requireNonNull(
			startDate,
			"시작일은 null일 수 없습니다."
		);
		this.endDate = Objects.requireNonNull(
			endDate,
			"종료일은 null일 수 없습니다."
		);

		if (!startDate.isBefore(endDate)) {
			throw new IllegalArgumentException("시작일(startDate)은 종료일(endDate)보다 이전이어야 합니다.");
		}
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