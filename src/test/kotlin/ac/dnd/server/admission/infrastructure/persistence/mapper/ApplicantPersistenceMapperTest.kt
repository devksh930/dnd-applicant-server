package ac.dnd.server.admission.infrastructure.persistence.mapper

import ac.dnd.server.admission.domain.model.EventData
import ac.dnd.server.admission.domain.model.ViewablePeriod
import ac.dnd.server.admission.infrastructure.persistence.entity.ApplicantEntity
import ac.dnd.server.admission.infrastructure.persistence.entity.EventEntity
import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.admission.domain.enums.ApplicantStatus
import ac.dnd.server.admission.domain.enums.ApplicantType
import ac.dnd.server.fixture.EventFixture
import ac.dnd.server.fixture.EventDataFixture
import ac.dnd.server.fixture.ViewablePeriodFixture
import ac.dnd.server.fixture.ApplicantFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

@UnitTest
class ApplicantPersistenceMapperTest : DescribeSpec({
    val applicantPersistenceMapper = ApplicantPersistenceMapper()

    describe("ApplicantPersistenceMapper") {

        it("Applicant_엔티티의_기본_필드들이_정상적으로_매핑되는지_확인한다") {
            // given
            val name = "테스트"
            val email = "test@test.com"
            val type = ApplicantType.BACKEND
            val status = ApplicantStatus.PASSED
            val event = EventFixture.create()

            val entity = ApplicantFixture.createForMapperTest(
                name = name,
                email = email,
                type = type,
                status = status,
                event = event
            )

            entity.name shouldBe name
            entity.email shouldBe email
            entity.type shouldBe type
            entity.status shouldBe status
            entity.event shouldBe event
        }

        it("이벤트_도메인에서_엔티티로_매핑한다") {
            // given
            val domain = EventDataFixture.createWithCustomValues(
                name = "테스트 이벤트",
                period = ViewablePeriodFixture.createForTesting(LocalDateTime.now())
            )

            // when
            val entity = applicantPersistenceMapper.eventDataToEntity(domain)

            // then
            entity.name shouldBe domain.name
            entity.period shouldBe domain.period
        }

        it("Event_엔티티의_기본_필드들이_정상적으로_설정되는지_확인한다") {
            // given
            val name = "테스트 이벤트"
            val period = ViewablePeriodFixture.createForTesting(LocalDateTime.now())
            val resultAnnouncementDateTime = LocalDateTime.now()
            val status = EventFixture.create().status

            val entity = EventFixture.createForMapperTest(
                name = name,
                period = period,
                resultAnnouncementDateTime = resultAnnouncementDateTime,
                status = status
            )

            // when & then
            entity.name shouldBe name
            entity.period shouldBe period
            entity.resultAnnouncementDateTime shouldBe resultAnnouncementDateTime
            entity.status shouldBe status
        }
    }
})
