package ac.dnd.server.fixture

import ac.dnd.server.admission.infrastructure.persistence.entity.ApplicantEntity
import ac.dnd.server.admission.domain.enums.ApplicantStatus
import ac.dnd.server.admission.domain.enums.ApplicantType

object ApplicantFixture {

    fun create(
        name: String,
        email: String,
        type: ApplicantType,
        status: ApplicantStatus
    ): ApplicantEntity {
        return ApplicantEntity(
            name,
            email,
            name,
            email,
            type,
            status
        )
    }

    fun create(): ApplicantEntity {
        val name = "홍길동"
        val email = "test@test.com"

        val applicant = ApplicantEntity(
            name,
            email,
            name,
            email,
            ApplicantType.BACKEND,
            ApplicantStatus.PASSED
        )

        applicant.withEvent(EventFixture.create())
        return applicant
    }

    fun createWithStatus(status: ApplicantStatus): ApplicantEntity {
        val name = "홍길동"
        val email = "test@test.com"
        return ApplicantEntity(
            name,
            email,
            name,
            email,
            ApplicantType.BACKEND,
            status
        )
    }

    fun createForMapperTest(
        name: String = "테스트",
        email: String = "test@test.com",
        type: ApplicantType = ApplicantType.BACKEND,
        status: ApplicantStatus = ApplicantStatus.PASSED,
        event: ac.dnd.server.admission.infrastructure.persistence.entity.EventEntity? = null
    ): ApplicantEntity {
        val applicant = ApplicantEntity(
            name,
            email,
            name,
            email,
            type,
            status
        )

        event?.let { applicant.withEvent(it) }
        return applicant
    }
}
