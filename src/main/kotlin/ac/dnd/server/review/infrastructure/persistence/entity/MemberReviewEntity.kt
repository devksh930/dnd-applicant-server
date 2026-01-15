package ac.dnd.server.review.infrastructure.persistence.entity

import ac.dnd.server.review.domain.enums.Position
import ac.dnd.server.review.domain.enums.SubmissionStatus
import ac.dnd.server.review.domain.value.GenerationInfo
import ac.dnd.server.shared.persistence.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDateTime

@Entity
class MemberReviewEntity(
    @Embedded
    val generationInfo: GenerationInfo,

    var name: String,

    @Enumerated(EnumType.STRING)
    val position: Position,

    @Column(length = 1000)
    var description: String? = null,

    @Enumerated(EnumType.STRING)
    var status: SubmissionStatus = SubmissionStatus.NONE,

    @Column(name = "submitted_at")
    var submittedAt: LocalDateTime? = null
) : BaseEntity() {

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


