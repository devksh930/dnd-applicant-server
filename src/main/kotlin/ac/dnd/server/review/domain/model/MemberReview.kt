package ac.dnd.server.review.domain.model

import ac.dnd.server.review.domain.enums.Position
import ac.dnd.server.review.domain.enums.SubmissionStatus
import ac.dnd.server.review.domain.value.GenerationInfo
import java.time.LocalDateTime

data class MemberReview(
    val id: Long,
    val generationInfo: GenerationInfo,
    var name: String,
    val position: Position,
    var description: String? = null,
    var status: SubmissionStatus = SubmissionStatus.NONE,
    var submittedAt: LocalDateTime? = null
) {
    fun update(
        name: String,
        description: String
    ) {
        this.name = name
        this.description = description
        this.status = SubmissionStatus.PENDING
        if (this.submittedAt == null) {
            this.submittedAt = LocalDateTime.now()
        }
    }

    fun isFirstSubmission(): Boolean {
        return this.submittedAt == null
    }
}
