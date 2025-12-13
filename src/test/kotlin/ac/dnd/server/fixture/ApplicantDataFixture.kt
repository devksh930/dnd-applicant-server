package ac.dnd.server.fixture

import ac.dnd.server.admission.domain.enums.ApplicantStatus
import ac.dnd.server.admission.domain.enums.ApplicantType
import ac.dnd.server.admission.domain.model.ApplicantData
import ac.dnd.server.admission.domain.model.ViewablePeriod
import java.time.LocalDateTime

object ApplicantDataFixture {

    fun create(
        id: Long = 1L,
        eventName: String = "테스트 이벤트",
        name: String = "홍길동",
        email: String = "test@test.com",
        type: ApplicantType = ApplicantType.BACKEND,
        status: ApplicantStatus = ApplicantStatus.PASSED,
        period: ViewablePeriod = ViewablePeriod.of(LocalDateTime.MIN, LocalDateTime.MAX)
    ): ApplicantData {
        return ApplicantData(
            id = id,
            eventName = eventName,
            name = name,
            email = email,
            type = type,
            status = status,
            period = period
        )
    }

    fun createViewable(now: LocalDateTime): ApplicantData {
        return create(
            period = ViewablePeriod.of(
                now.minusDays(10),
                now.plusDays(10)
            )
        )
    }

    fun createNotViewable(now: LocalDateTime): ApplicantData {
        return create(
            period = ViewablePeriod.of(
                now.minusDays(20),
                now.minusDays(10)
            )
        )
    }

    fun createWithStatus(status: ApplicantStatus): ApplicantData {
        return create(status = status)
    }
}
