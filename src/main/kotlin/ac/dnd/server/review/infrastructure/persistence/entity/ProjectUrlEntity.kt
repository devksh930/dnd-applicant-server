package ac.dnd.server.review.infrastructure.persistence.entity

import ac.dnd.server.review.domain.enums.UrlType
import ac.dnd.server.shared.persistence.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.ManyToOne

@Entity
class ProjectUrlEntity(

    @ManyToOne(optional = false)
    val project: ProjectEntity,

    @Enumerated(EnumType.STRING)
    val type: UrlType,

    val link: String,

    @Column(name = "sort_order")
    val order: Int
) : BaseEntity() {
}