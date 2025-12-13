package ac.dnd.server.review.infrastructure.persistence.entity

import ac.dnd.server.review.domain.enums.SubmissionStatus
import ac.dnd.server.review.domain.value.GenerationInfo
import ac.dnd.server.shared.persistence.BaseEntity
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity
class Project(
    @Embedded
    val generationInfo: GenerationInfo,
    val name: String,
    val description: String,
    val techStack: String,
    @Enumerated(EnumType.STRING)
    val status: SubmissionStatus = SubmissionStatus.NONE
) : BaseEntity() {
}