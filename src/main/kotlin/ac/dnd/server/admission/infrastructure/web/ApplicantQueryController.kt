package ac.dnd.server.admission.infrastructure.web

import ac.dnd.server.admission.application.service.ApplicantQueryService
import ac.dnd.server.admission.infrastructure.web.dto.request.ApplicantStatusCheckRequest
import ac.dnd.server.admission.infrastructure.web.dto.response.ApplicantCheckQueryResponse
import ac.dnd.server.admission.infrastructure.web.mapper.ApplicantWebMapper
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/event/{eventId}/applicant")
class ApplicantQueryController(
    private val applicantQueryService: ApplicantQueryService,
    private val applicantWebMapper: ApplicantWebMapper
) {
    @PostMapping("/status/check")
    fun checkApplicantStatus(
        @Valid @RequestBody request: ApplicantStatusCheckRequest,
        @PathVariable eventId: Long
    ): ResponseEntity<ApplicantCheckQueryResponse> {
        val applicant = applicantQueryService.getApplicantStatusCheck(
            applicantWebMapper.statusCheckRequestToCommand(
                request,
                eventId
            )
        )

        return ResponseEntity.ok(
            ApplicantCheckQueryResponse(
                applicant.eventName,
                applicant.name,
                applicant.status
            )
        )
    }
}