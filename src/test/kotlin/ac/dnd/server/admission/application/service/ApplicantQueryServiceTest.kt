package ac.dnd.server.admission.application.service

import ac.dnd.server.admission.application.dto.ApplicantStatusCheckCommand
import ac.dnd.server.admission.domain.AdmissionRepository
import ac.dnd.server.admission.domain.ApplicantValidator
import ac.dnd.server.admission.domain.model.ViewablePeriod
import ac.dnd.server.admission.exception.ApplicantNotFoundException
import ac.dnd.server.admission.exception.ApplicantViewablePeriodEndException
import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.fixture.ApplicantDataFixture
import ac.dnd.server.shared.event.EventDispatcher
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.mockito.kotlin.any
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.context.ApplicationEventPublisher
import java.time.LocalDateTime

@UnitTest
class ApplicantQueryServiceTest : DescribeSpec({

    val admissionRepository = mock<AdmissionRepository>()
    val applicantValidator = mock<ApplicantValidator>()
    val mockEventPublisher = mock<ApplicationEventPublisher>()
    val applicantQueryService = ApplicantQueryService(admissionRepository, applicantValidator)

    beforeSpec {
        EventDispatcher.setApplicationEventPublisher(mockEventPublisher)
    }

    describe("getApplicantStatusCheck") {

        it("지원자가 존재하고 조회 가능 기간 내이면 ApplicantData를 반환한다") {
            // given
            val now = LocalDateTime.now()
            val command = ApplicantStatusCheckCommand(
                eventId = 1L,
                name = "홍길동",
                email = "test@example.com"
            )
            val applicantData = ApplicantDataFixture.createViewable(now)

            whenever(admissionRepository.findAdmissionByEventIdAndNameAndEmail(
                command.eventId,
                command.name,
                command.email
            )).thenReturn(applicantData)

            // when
            val result = applicantQueryService.getApplicantStatusCheck(command)

            // then
            result shouldBe applicantData
            verify(applicantValidator).viewableValidator(any(), any())
        }

        it("지원자가 존재하지 않으면 ApplicantNotFoundException이 발생한다") {
            // given
            val command = ApplicantStatusCheckCommand(
                eventId = 1L,
                name = "존재하지않는이름",
                email = "notfound@example.com"
            )

            whenever(admissionRepository.findAdmissionByEventIdAndNameAndEmail(
                command.eventId,
                command.name,
                command.email
            )).thenReturn(null)

            // when & then
            shouldThrow<ApplicantNotFoundException> {
                applicantQueryService.getApplicantStatusCheck(command)
            }
        }

        it("조회 가능 기간이 지났으면 ApplicantViewablePeriodEndException이 발생한다") {
            // given
            val now = LocalDateTime.now()
            val command = ApplicantStatusCheckCommand(
                eventId = 1L,
                name = "홍길동",
                email = "test@example.com"
            )
            val applicantData = ApplicantDataFixture.create(
                period = ViewablePeriod.of(
                    now.minusDays(20),
                    now.minusDays(10)
                )
            )

            whenever(admissionRepository.findAdmissionByEventIdAndNameAndEmail(
                command.eventId,
                command.name,
                command.email
            )).thenReturn(applicantData)

            doThrow(ApplicantViewablePeriodEndException())
                .whenever(applicantValidator).viewableValidator(any(), any())

            // when & then
            shouldThrow<ApplicantViewablePeriodEndException> {
                applicantQueryService.getApplicantStatusCheck(command)
            }
        }
    }
})