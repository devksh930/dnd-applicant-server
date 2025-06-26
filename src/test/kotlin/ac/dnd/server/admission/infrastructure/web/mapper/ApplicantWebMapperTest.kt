package ac.dnd.server.admission.infrastructure.web.mapper

import ac.dnd.server.admission.application.dto.ApplicantStatusCheckCommand
import ac.dnd.server.admission.infrastructure.web.dto.request.ApplicantStatusCheckRequest
import ac.dnd.server.annotation.UnitTest
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

@UnitTest
class ApplicantWebMapperTest : DescribeSpec({
    val applicantWebMapper = ApplicantWebMapper()

    describe("ApplicantWebMapper") {
        
        it("statusCheckRequestToCommand 메서드가 정상적으로 변환한다") {
            // given
            val request = ApplicantStatusCheckRequest(
                "name",
                "email"
            )
            val eventId = 1L

            // when
            val result = applicantWebMapper.statusCheckRequestToCommand(request, eventId)

            // then
            val expected = ApplicantStatusCheckCommand(
                1L,
                "name",
                "email"
            )
            result shouldBe expected
        }
    }
})