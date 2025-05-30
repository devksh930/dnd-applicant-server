package ac.dnd.server.admission.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import ac.dnd.server.admission.domain.model.ViewablePeriod;
import ac.dnd.server.annotation.UnitTest;

@UnitTest
class ViewablePeriodTest {

	@Test
	void ViewablePeriod_객체를_성공적으로_생성한다() {
		// given
		LocalDateTime startDate = LocalDateTime.of(
			2024,
			1,
			1,
			0,
			0
		);
		LocalDateTime endDate = LocalDateTime.of(
			2024,
			1,
			31,
			23,
			59
		);

		// when
		ViewablePeriod period = ViewablePeriod.of(
			startDate,
			endDate
		);

		// then
		assertThat(period.getStartDate()).isEqualTo(startDate);
		assertThat(period.getEndDate()).isEqualTo(endDate);
	}

	@Test
	void startDate가_null이면_IllegalArgumentException이_발생한다() {
		// given
		LocalDateTime endDate = LocalDateTime.of(
			2024,
			1,
			31,
			23,
			59
		);

		// when & then
		assertThatThrownBy(() -> ViewablePeriod.of(
			null,
			endDate
		)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void endDate가_null이면_IllegalArgumentException이_발생한다() {
		// given
		LocalDateTime startDate = LocalDateTime.of(
			2024,
			1,
			1,
			0,
			0
		);

		// when & then
		assertThatThrownBy(() -> ViewablePeriod.of(
			startDate,
			null
		)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void startDate가_endDate보다_이후이면_IllegalArgumentException이_발생한다() {
		// given
		LocalDateTime startDate = LocalDateTime.of(
			2024,
			1,
			31,
			0,
			0
		);
		LocalDateTime endDate = LocalDateTime.of(
			2024,
			1,
			1,
			0,
			0
		);

		// when & then
		assertThatThrownBy(() -> ViewablePeriod.of(
			startDate,
			endDate
		)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void startDate가_endDate와_같으면_IllegalArgumentException이_발생한다() {
		// given
		LocalDateTime startDate = LocalDateTime.of(
			2024,
			1,
			1,
			0,
			0
		);
		LocalDateTime endDate = LocalDateTime.of(
			2024,
			1,
			1,
			0,
			0
		);

		// when & then
		assertThatThrownBy(() -> ViewablePeriod.of(
			startDate,
			endDate
		)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 주어진_시각이_기간에_포함되면_true를_반환한다() {
		// given
		LocalDateTime startDate = LocalDateTime.of(
			2024,
			1,
			1,
			0,
			0
		);
		LocalDateTime endDate = LocalDateTime.of(
			2024,
			1,
			31,
			23,
			59
		);
		ViewablePeriod period = ViewablePeriod.of(
			startDate,
			endDate
		);
		LocalDateTime now = LocalDateTime.of(
			2024,
			1,
			15,
			12,
			0
		);

		// when
		boolean result = period.contains(now);

		// then
		assertThat(result).isTrue();
	}

	@Test
	void 주어진_시각이_startDate보다_이전이면_false를_반환한다() {
		// given
		LocalDateTime startDate = LocalDateTime.of(
			2024,
			1,
			1,
			0,
			0
		);
		LocalDateTime endDate = LocalDateTime.of(
			2024,
			1,
			31,
			23,
			59
		);
		ViewablePeriod period = ViewablePeriod.of(
			startDate,
			endDate
		);
		LocalDateTime now = LocalDateTime.of(
			2023,
			12,
			31,
			23,
			59
		);

		// when
		boolean result = period.contains(now);

		// then
		assertThat(result).isFalse();
	}

	@Test
	void 주어진_시각이_endDate보다_이후이면_false를_반환한다() {
		// given
		LocalDateTime startDate = LocalDateTime.of(
			2024,
			1,
			1,
			0,
			0
		);
		LocalDateTime endDate = LocalDateTime.of(
			2024,
			1,
			31,
			23,
			59
		);
		ViewablePeriod period = ViewablePeriod.of(
			startDate,
			endDate
		);
		LocalDateTime now = LocalDateTime.of(
			2024,
			2,
			1,
			0,
			0
		);

		// when
		boolean result = period.contains(now);

		// then
		assertThat(result).isFalse();
	}

	@Test
	void 주어진_시각이_startDate와_같으면_false를_반환한다() {
		// given
		LocalDateTime startDate = LocalDateTime.of(
			2024,
			1,
			1,
			0,
			0
		);
		LocalDateTime endDate = LocalDateTime.of(
			2024,
			1,
			31,
			23,
			59
		);
		ViewablePeriod period = ViewablePeriod.of(
			startDate,
			endDate
		);
		LocalDateTime now = LocalDateTime.of(
			2024,
			1,
			1,
			0,
			0
		);

		// when
		boolean result = period.contains(now);

		// then
		assertThat(result).isFalse();
	}

	@Test
	void 주어진_시각이_endDate와_같으면_false를_반환한다() {
		// given
		LocalDateTime startDate = LocalDateTime.of(
			2024,
			1,
			1,
			0,
			0
		);
		LocalDateTime endDate = LocalDateTime.of(
			2024,
			1,
			31,
			23,
			59
		);
		ViewablePeriod period = ViewablePeriod.of(
			startDate,
			endDate
		);
		LocalDateTime now = LocalDateTime.of(
			2024,
			1,
			31,
			23,
			59
		);

		// when
		boolean result = period.contains(now);

		// then
		assertThat(result).isFalse();
	}
}