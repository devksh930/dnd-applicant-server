package ac.dnd.server.review.domain.model

import ac.dnd.server.review.domain.enums.SubmissionStatus
import ac.dnd.server.review.domain.value.GenerationInfo
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

data class Project(
    val generationInfo: GenerationInfo,
    val name: String,
    val description: String,
    val techStacks: List<String> = emptyList(),
    val fileId: Long? = null,
    val status: SubmissionStatus = SubmissionStatus.NONE
)