package ac.dnd.server.fixture

import ac.dnd.server.admission.domain.model.ApplicantData
import ac.dnd.server.admission.domain.model.ViewablePeriod
import ac.dnd.server.enums.ApplicantStatus
import ac.dnd.server.enums.ApplicantType
import java.time.LocalDateTime

object ApplicantDataFixture {
    fun create(): ApplicantData {
        return ApplicantData(
            1L,
            "테스트 이벤트",
            "홍길동",
            "test@test.com",
            ApplicantType.BACKEND,
            ApplicantStatus.PASSED,
            ViewablePeriod.of(
                LocalDateTime.MIN,
                LocalDateTime.MAX
            )
        )
    }
}