package ac.dnd.server.fixture

import ac.dnd.server.admission.infrastructure.persistence.entity.Applicant
import ac.dnd.server.enums.ApplicantStatus
import ac.dnd.server.enums.ApplicantType

object ApplicantFixture {

    fun create(
        name: String,
        email: String,
        type: ApplicantType,
        status: ApplicantStatus
    ): Applicant {
        return Applicant(
            name,
            email,
            name,
            email,
            type,
            status
        )
    }

    fun create(): Applicant {
        val name = "홍길동"
        val email = "test@test.com"

        val applicant = Applicant(
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

    fun createWithStatus(status: ApplicantStatus): Applicant {
        val name = "홍길동"
        val email = "test@test.com"
        return Applicant(
            name,
            email,
            name,
            email,
            ApplicantType.BACKEND,
            status
        )
    }
}