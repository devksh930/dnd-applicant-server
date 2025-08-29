package ac.dnd.server.admission.application.service

import ac.dnd.server.admission.application.dto.ApplicantStatusCheckCommand
import ac.dnd.server.admission.domain.AdmissionRepository
import ac.dnd.server.admission.domain.ApplicantValidator
import ac.dnd.server.admission.domain.event.ApplicantQueryEvent
import ac.dnd.server.admission.domain.model.ApplicantData
import ac.dnd.server.admission.exception.ApplicantNotFoundException
import ac.dnd.server.admission.infrastructure.persistence.crypto.HmacBlindIndexCreator
import ac.dnd.server.shared.event.Events
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ApplicantQueryService(
    private val admissionRepository: AdmissionRepository,
    private val applicantValidator: ApplicantValidator,
    private val hmacBlindIndexCreator: HmacBlindIndexCreator
) {
    private val log = LoggerFactory.getLogger(ApplicantQueryService::class.java)

    @Cacheable(value = ["applicantStatus"], key = "#command.eventId + ':' + #command.name + ':' + #command.email")
    fun getApplicantStatusCheck(command: ApplicantStatusCheckCommand): ApplicantData {
        val applicant = admissionRepository.findAdmissionByEventIdAndBlindIndexes(
            command.eventId,
            hmacBlindIndexCreator.create(command.name),
            hmacBlindIndexCreator.create(command.email)
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
