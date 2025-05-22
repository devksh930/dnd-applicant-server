package ac.dnd.server.admission.infrastructure.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ac.dnd.server.admission.domain.event.ApplicantQueryEvent;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ApplicantEventListener {

	@EventListener(classes = ApplicantEventListener.class)
	public void handleApplicantEvent(
		final ApplicantQueryEvent event
	) {
		log.info(
			"Applicant query event: {}",
			event
		);
	}
}
