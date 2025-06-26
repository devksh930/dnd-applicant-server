package ac.dnd.server.admission.infrastructure.persistence.mapper

import ac.dnd.server.admission.domain.model.EventData
import ac.dnd.server.admission.domain.model.ViewablePeriod
import ac.dnd.server.admission.infrastructure.persistence.entity.Applicant
import ac.dnd.server.admission.infrastructure.persistence.entity.Event
import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.enums.ApplicantStatus
import ac.dnd.server.enums.ApplicantType
import ac.dnd.server.fixture.EventFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.test.util.ReflectionTestUtils
import java.time.LocalDateTime

@UnitTest
class ApplicantPersistenceMapperTest : DescribeSpec({
    val applicantPersistenceMapper = ApplicantPersistenceMapper()

    describe("ApplicantPersistenceMapper") {

        it("모든_필드가_채워진_엔티티를_정상적으로_매핑한다") {
            // given
            val name = "테스트"
            val email = "test@test.com"
            val type = ApplicantType.BACKEND
            val status = ApplicantStatus.PASSED
            val event = EventFixture.create()

            // 컨버터 이슈를 피하기 위해 protected 생성자를 사용하여 엔티티 생성
            val constructor = Applicant::class.java.getDeclaredConstructor()
            constructor.isAccessible = true
            val entity = constructor.newInstance()

            // 컨버터를 우회하기 위해 ReflectionTestUtils를 사용하여 필드 설정
            ReflectionTestUtils.setField(entity, "name", name)
            ReflectionTestUtils.setField(entity, "email", email)
            ReflectionTestUtils.setField(entity, "nameBlindIndex", name)
            ReflectionTestUtils.setField(entity, "emailBlindIndex", email)
            ReflectionTestUtils.setField(entity, "type", type)
            ReflectionTestUtils.setField(entity, "status", status)
            ReflectionTestUtils.setField(entity, "id", 1L)

            entity.withEvent(event)

            // when
            val applicantData = applicantPersistenceMapper.applicantEntityToDomain(entity)

            // then
            applicantData.name shouldBe name
            applicantData.email shouldBe email
            applicantData.type shouldBe type
            applicantData.status shouldBe status
        }

        it("이벤트_도메인에서_엔티티로_매핑한다") {
            // given
            val name = "테스트 이벤트"
            val period = ViewablePeriod.of(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
            )
            val domain = EventData(
                1L,
                name,
                period,
                LocalDateTime.now(),
                EventFixture.create().status
            )

            // when
            val entity = applicantPersistenceMapper.eventDataToEntity(domain)

            // then
            entity.name shouldBe name
            entity.period shouldBe period
        }

        it("이벤트_엔티티에서_도메인으로_매핑한다") {
            // given
            val id = 1L
            val name = "테스트 이벤트"
            val period = ViewablePeriod.of(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
            )
            val entity = Event(
                name,
                period,
                LocalDateTime.now(),
                EventFixture.create().status
            )
            ReflectionTestUtils.setField(
                entity,
                "id",
                id
            )

            // when
            val domain = applicantPersistenceMapper.eventEntityToDomain(entity)

            // then
            domain.id shouldBe id
            domain.name shouldBe name
            domain.period shouldBe period
        }
    }
})
