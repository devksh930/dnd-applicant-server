package ac.dnd.server.review.infrastructure.persistence.entity

import ac.dnd.server.review.domain.enums.UrlType
import ac.dnd.server.shared.persistence.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.ManyToOne

@Entity
class ProjectUrl(

    @ManyToOne(optional = false)
    val project: Project,

    @Enumerated(EnumType.STRING)
    val type: UrlType,

    val link: String,

    val order: Int
) : BaseEntity() {
}