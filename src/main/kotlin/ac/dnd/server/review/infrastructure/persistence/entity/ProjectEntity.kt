package ac.dnd.server.review.infrastructure.persistence.entity

import ac.dnd.server.review.domain.enums.SubmissionStatus
import ac.dnd.server.review.domain.value.GenerationInfo
import ac.dnd.server.review.domain.value.TechStacks
import ac.dnd.server.review.infrastructure.persistence.converter.TechStacksConverter
import ac.dnd.server.shared.persistence.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDateTime

@Entity
class ProjectEntity(
    @Embedded
    val generationInfo: GenerationInfo,
    var name: String,
    var description: String,
    @Convert(converter = TechStacksConverter::class)
    var techStacks: TechStacks = TechStacks(),
    @Column(name = "file_id")
    var fileId: Long? = null,
    @Enumerated(EnumType.STRING)
    var status: SubmissionStatus = SubmissionStatus.NONE,
    @Column(name = "submitted_at")
    var submittedAt: LocalDateTime? = null, // null이면 아직 사용자가 등록 안 함

) : BaseEntity() {

    fun update(
        name: String,
        description: String,
        techStacks: TechStacks,
        fileId: Long?
    ) {
        this.name = name
        this.description = description
        this.techStacks = techStacks
        if (this.fileId != fileId) {
            this.fileId = fileId
        }
        // 처음 업데이트 하는 경우에만 최초 시간 기록
        if (this.submittedAt == null) {
            this.submittedAt = LocalDateTime.now()
        }
    }

    fun isFirstSubmission(): Boolean {
        return this.submittedAt == null
    }

}