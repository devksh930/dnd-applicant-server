package ac.dnd.server.review.domain.model

import ac.dnd.server.review.domain.enums.SubmissionStatus
import ac.dnd.server.review.domain.value.GenerationInfo
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

data class Project(
    val generationInfo: GenerationInfo,
    val description: String,
    val techStack: String,
    @Enumerated(EnumType.STRING)
    val status: SubmissionStatus = SubmissionStatus.NONE

) {
}