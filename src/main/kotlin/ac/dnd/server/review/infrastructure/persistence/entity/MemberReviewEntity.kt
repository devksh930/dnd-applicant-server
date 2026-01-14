package ac.dnd.server.review.infrastructure.persistence.entity

import ac.dnd.server.review.domain.enums.SubmissionStatus
import ac.dnd.server.shared.persistence.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity
class MemberReviewEntity(
    val name: String,

    @Column(length = 1000)
    val description: String? = null,

    @Enumerated(EnumType.STRING)
    val status: SubmissionStatus = SubmissionStatus.NONE
) : BaseEntity() {
}


