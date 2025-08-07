package ac.dnd.server.admission.infrastructure.web.mapper

import ac.dnd.server.admission.application.dto.ApplicantStatusCheckCommand
import ac.dnd.server.admission.infrastructure.web.dto.request.ApplicantStatusCheckRequest
import org.springframework.stereotype.Component

@Component
class ApplicantWebMapper {

    fun statusCheckRequestToCommand(
        request: ApplicantStatusCheckRequest,
        eventId: Long
    ): ApplicantStatusCheckCommand {
        return ApplicantStatusCheckCommand(
            eventId,
            request.name,
            request.email
        )
    }
}
