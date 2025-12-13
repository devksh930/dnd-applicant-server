package ac.dnd.server.admission.domain.model

import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.fixture.ApplicantDataFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

@UnitTest
class ApplicantDataTest : DescribeSpec({

    describe("isViewable") {

        it("현재 시각이 조회 가능 기간 내이면 true를 반환한다") {
            // given
            val now = LocalDateTime.now()
            val applicant = ApplicantDataFixture.createViewable(now)

            // when
            val result = applicant.isViewable(now)

            // then
            result shouldBe true
        }

        it("현재 시각이 조회 가능 기간 이전이면 false를 반환한다") {
            // given
            val now = LocalDateTime.now()
            val applicant = ApplicantDataFixture.create(
                period = ViewablePeriod.of(
                    now.plusDays(10),
                    now.plusDays(20)
                )
            )

            // when
            val result = applicant.isViewable(now)

            // then
            result shouldBe false
        }

        it("현재 시각이 조회 가능 기간 이후이면 false를 반환한다") {
            // given
            val now = LocalDateTime.now()
            val applicant = ApplicantDataFixture.createNotViewable(now)

            // when
            val result = applicant.isViewable(now)

            // then
            result shouldBe false
        }

        it("시작 시각과 정확히 같으면 false를 반환한다") {
            // given
            val now = LocalDateTime.of(2024, 6, 1, 12, 0)
            val applicant = ApplicantDataFixture.create(
                period = ViewablePeriod.of(
                    now,
                    now.plusDays(10)
                )
            )

            // when
            val result = applicant.isViewable(now)

            // then
            result shouldBe false
        }

        it("종료 시각과 정확히 같으면 false를 반환한다") {
            // given
            val now = LocalDateTime.of(2024, 6, 10, 12, 0)
            val applicant = ApplicantDataFixture.create(
                period = ViewablePeriod.of(
                    now.minusDays(10),
                    now
                )
            )

            // when
            val result = applicant.isViewable(now)

            // then
            result shouldBe false
        }
    }
})