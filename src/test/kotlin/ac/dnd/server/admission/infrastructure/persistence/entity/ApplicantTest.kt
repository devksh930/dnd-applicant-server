package ac.dnd.server.admission.infrastructure.persistence.entity

import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.enums.ApplicantStatus
import ac.dnd.server.enums.ApplicantType
import ac.dnd.server.fixture.ApplicantFixture
import ac.dnd.server.fixture.EventFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

@UnitTest
class ApplicantTest : DescribeSpec({

    describe("Applicant") {

        it("Applicant 객체를 생성하고 필드 값을 확인한다") {
            // given
            val name = "홍길동"
            val email = "gildong@example.com"
            val type = ApplicantType.FRONTEND
            val status = ApplicantStatus.PASSED

            // when
            val applicant = Applicant(
                name,
                email,
                name,
                email,
                type,
                status
            )

            // then
            applicant.name shouldBe name
            applicant.email shouldBe email
            applicant.type shouldBe type
            applicant.status shouldBe status
            applicant.event shouldBe null
        }

        it("withEvent 메서드로 Applicant에 Event를 설정한다") {
            // given
            val applicant = ApplicantFixture.create()
            val event = EventFixture.create()

            // when
            applicant.withEvent(event)

            // then
            applicant.event shouldNotBe null
            applicant.event shouldBe event
        }
    }
})