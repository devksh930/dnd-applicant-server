package ac.dnd.server.admission.domain

import ac.dnd.server.admission.domain.model.ViewablePeriod
import ac.dnd.server.annotation.UnitTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

@UnitTest
class ViewablePeriodTest : DescribeSpec({

    describe("ViewablePeriod") {

        it("ViewablePeriod 객체를 성공적으로 생성한다") {
            // given
            val startDate = LocalDateTime.of(2024, 1, 1, 0, 0)
            val endDate = LocalDateTime.of(2024, 1, 31, 23, 59)

            // when
            val period = ViewablePeriod.of(startDate, endDate)

            // then
            period.startDate shouldBe startDate
            period.endDate shouldBe endDate
        }


        it("startDate가 endDate보다 이후이면 IllegalArgumentException이 발생한다") {
            // given
            val startDate = LocalDateTime.of(2024, 1, 31, 0, 0)
            val endDate = LocalDateTime.of(2024, 1, 1, 0, 0)

            // when & then
            shouldThrow<IllegalArgumentException> {
                ViewablePeriod.of(startDate, endDate)
            }
        }

        it("startDate가 endDate와 같으면 정상적으로 생성된다") {
            // given
            val startDate = LocalDateTime.of(2024, 1, 1, 0, 0)
            val endDate = LocalDateTime.of(2024, 1, 1, 0, 0)

            // when
            val period = ViewablePeriod.of(startDate, endDate)

            // then
            period.startDate shouldBe startDate
            period.endDate shouldBe endDate
        }

        it("주어진 시각이 기간에 포함되면 true를 반환한다") {
            // given
            val startDate = LocalDateTime.of(2024, 1, 1, 0, 0)
            val endDate = LocalDateTime.of(2024, 1, 31, 23, 59)
            val period = ViewablePeriod.of(startDate, endDate)
            val now = LocalDateTime.of(2024, 1, 15, 12, 0)

            // when
            val result = period.contains(now)

            // then
            result shouldBe true
        }

        it("주어진 시각이 startDate보다 이전이면 false를 반환한다") {
            // given
            val startDate = LocalDateTime.of(2024, 1, 1, 0, 0)
            val endDate = LocalDateTime.of(2024, 1, 31, 23, 59)
            val period = ViewablePeriod.of(startDate, endDate)
            val now = LocalDateTime.of(2023, 12, 31, 23, 59)

            // when
            val result = period.contains(now)

            // then
            result shouldBe false
        }

        it("주어진 시각이 endDate보다 이후이면 false를 반환한다") {
            // given
            val startDate = LocalDateTime.of(2024, 1, 1, 0, 0)
            val endDate = LocalDateTime.of(2024, 1, 31, 23, 59)
            val period = ViewablePeriod.of(startDate, endDate)
            val now = LocalDateTime.of(2024, 2, 1, 0, 0)

            // when
            val result = period.contains(now)

            // then
            result shouldBe false
        }

        it("주어진 시각이 startDate와 같으면 false를 반환한다") {
            // given
            val startDate = LocalDateTime.of(2024, 1, 1, 0, 0)
            val endDate = LocalDateTime.of(2024, 1, 31, 23, 59)
            val period = ViewablePeriod.of(startDate, endDate)
            val now = LocalDateTime.of(2024, 1, 1, 0, 0)

            // when
            val result = period.contains(now)

            // then
            result shouldBe false
        }

        it("주어진 시각이 endDate와 같으면 false를 반환한다") {
            // given
            val startDate = LocalDateTime.of(2024, 1, 1, 0, 0)
            val endDate = LocalDateTime.of(2024, 1, 31, 23, 59)
            val period = ViewablePeriod.of(startDate, endDate)
            val now = LocalDateTime.of(2024, 1, 31, 23, 59)

            // when
            val result = period.contains(now)

            // then
            result shouldBe false
        }
    }
})
