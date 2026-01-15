package ac.dnd.server.review.application.dto.command

data class MemberReviewSubmitCommand(
    val linkKey: String,
    val name: String,
    val description: String,
    val urlLinks: List<UrlLinkCommand>?
)
