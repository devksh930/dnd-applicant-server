package ac.dnd.server.admission.domain

import ac.dnd.server.admission.domain.model.ApplicantData
import ac.dnd.server.admission.exception.ApplicantViewablePeriodEndException
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ApplicantValidator {

    fun viewableValidator(applicant: ApplicantData, now: LocalDateTime) {
        if (!applicant.isViewable(now)) {
            throw ApplicantViewablePeriodEndException()
        }
    }
}