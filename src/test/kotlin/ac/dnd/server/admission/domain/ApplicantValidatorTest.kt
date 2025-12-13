package ac.dnd.server.admission.domain

import ac.dnd.server.admission.exception.ApplicantViewablePeriodEndException
import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.fixture.ApplicantDataFixture
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import java.time.LocalDateTime

@UnitTest
class ApplicantValidatorTest : DescribeSpec({

    val applicantValidator = ApplicantValidator()

    describe("viewableValidator") {

        it("조회 가능 기간 내이면 예외가 발생하지 않는다") {
            // given
            val now = LocalDateTime.now()
            val applicant = ApplicantDataFixture.createViewable(now)

            // when & then
            shouldNotThrowAny {
                applicantValidator.viewableValidator(applicant, now)
            }
        }

        it("조회 가능 기간이 지났으면 ApplicantViewablePeriodEndException이 발생한다") {
            // given
            val now = LocalDateTime.now()
            val applicant = ApplicantDataFixture.createNotViewable(now)

            // when & then
            shouldThrow<ApplicantViewablePeriodEndException> {
                applicantValidator.viewableValidator(applicant, now)
            }
        }

        it("조회 가능 기간 시작 전이면 ApplicantViewablePeriodEndException이 발생한다") {
            // given
            val now = LocalDateTime.now()
            val applicant = ApplicantDataFixture.create(
                period = ac.dnd.server.admission.domain.model.ViewablePeriod.of(
                    now.plusDays(10),
                    now.plusDays(20)
                )
            )

            // when & then
            shouldThrow<ApplicantViewablePeriodEndException> {
                applicantValidator.viewableValidator(applicant, now)
            }
        }
    }
})
