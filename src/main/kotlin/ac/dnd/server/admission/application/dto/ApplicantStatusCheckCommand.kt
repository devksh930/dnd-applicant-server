package ac.dnd.server.admission.application.dto

data class ApplicantStatusCheckCommand(
    val eventId: Long,
    val name: String,
    val email: String
)