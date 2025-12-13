package ac.dnd.server.review.infrastructure.persistence.entity

import ac.dnd.server.review.domain.value.GenerationInfo
import ac.dnd.server.shared.persistence.BaseEntity
import jakarta.persistence.Embedded
import jakarta.persistence.Entity

@Entity
class Project(
    @Embedded
    val generationInfo: GenerationInfo,
    val description: String,
    val techStack: String,

    ) : BaseEntity() {
}