package ac.dnd.server.admission.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ac.dnd.server.admission.application.dto.ApplicantData;
import ac.dnd.server.admission.application.dto.ApplicantStatusCheckCommand;
import ac.dnd.server.admission.application.service.ApplicantQueryService;
import ac.dnd.server.admission.presentation.dto.request.ApplicatStatusCheckRequest;
import ac.dnd.server.admission.presentation.dto.response.ApplicantCheckQueryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/event/{eventId}/applicant")
@RequiredArgsConstructor
public class ApplicantQueryController {
	private final ApplicantQueryService applicantQueryService;

	@PostMapping("/status/check")
	public ResponseEntity<ApplicantCheckQueryResponse> checkApplicantStatus(
		@Valid @RequestBody final ApplicatStatusCheckRequest request,
		@PathVariable final Long eventId
	) {
		final ApplicantData applicant = applicantQueryService.getApplicantStatusCheck(
			new ApplicantStatusCheckCommand(
				eventId,
				request.name(),
				request.email()
			));

		return ResponseEntity.ok(new ApplicantCheckQueryResponse(
			applicant.eventName(),
			applicant.name(),
			applicant.status()
		));
	}
}
