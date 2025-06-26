package ac.dnd.server.admission.application.service

import ac.dnd.server.admission.application.dto.ApplicantStatusCheckCommand
import ac.dnd.server.admission.domain.AdmissionRepository
import ac.dnd.server.admission.domain.ApplicantValidator
import ac.dnd.server.admission.domain.event.ApplicantQueryEvent
import ac.dnd.server.admission.domain.model.ApplicantData
import ac.dnd.server.admission.exception.ApplicantNotFoundException
import ac.dnd.server.common.event.Events
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ApplicantQueryService(
    private val admissionRepository: AdmissionRepository,
    private val applicantValidator: ApplicantValidator
) {
    private val log = LoggerFactory.getLogger(ApplicantQueryService::class.java)

    fun getApplicantStatusCheck(command: ApplicantStatusCheckCommand): ApplicantData {
        val applicant = admissionRepository.findAdmissionByEventIdAndNameAndEmail(
            command.eventId,
            command.name,
            command.email
        ).orElseThrow { ApplicantNotFoundException() }

        applicantValidator.viewableValidator(
            applicant,
            LocalDateTime.now()
        )
        publishedApplicantEvent(applicant)

        return applicant
    }

    private fun publishedApplicantEvent(applicant: ApplicantData) {
        Events.raise(
            ApplicantQueryEvent(
                applicant.id,
                applicant.name,
                applicant.email,
                applicant.type,
                applicant.status
            )
        )
    }
}
