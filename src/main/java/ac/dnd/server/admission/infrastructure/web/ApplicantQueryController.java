package ac.dnd.server.admission.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ac.dnd.server.admission.application.service.ApplicantQueryService;
import ac.dnd.server.admission.domain.model.ApplicantData;
import ac.dnd.server.admission.infrastructure.web.dto.request.ApplicantStatusCheckRequest;
import ac.dnd.server.admission.infrastructure.web.dto.response.ApplicantCheckQueryResponse;
import ac.dnd.server.admission.infrastructure.web.mapper.ApplicantWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/event/{eventId}/applicant")
@RequiredArgsConstructor
public class ApplicantQueryController {
	private final ApplicantQueryService applicantQueryService;
	private final ApplicantWebMapper applicantWebMapper;

	@PostMapping("/status/check")
	public ResponseEntity<ApplicantCheckQueryResponse> checkApplicantStatus(
		@Valid @RequestBody final ApplicantStatusCheckRequest request,
		@PathVariable final Long eventId
	) {
		final ApplicantData applicant = applicantQueryService.getApplicantStatusCheck(
			applicantWebMapper.statusCheckRequestToCommand(
				request,
				eventId
			)
		);

		return ResponseEntity.ok(new ApplicantCheckQueryResponse(
			applicant.eventName(),
			applicant.name(),
			applicant.status()
		));
	}
}
