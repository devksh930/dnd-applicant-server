package ac.dnd.server.enums

enum class ApplicantStatus(val description: String) {
    NONE("미정"),
    PASSED("합격"),
    FAILED("불합격"),
    WAITLISTED("예비 후보자")
}