package ac.dnd.server.review.domain.enums

enum class SubmissionStatus(
    val description: String
) {
    NONE("미제출"),
    PENDING("검수전"),
    PUBLISHED("검수완료"),
    REJECT("반려")
}