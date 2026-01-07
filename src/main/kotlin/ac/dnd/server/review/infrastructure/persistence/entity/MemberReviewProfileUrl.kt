package ac.dnd.server.review.infrastructure.persistence.entity

import ac.dnd.server.review.domain.value.GenerationInfo
import ac.dnd.server.review.domain.enums.UrlType
import ac.dnd.server.shared.persistence.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.ManyToOne

@Entity
class MemberReviewProfileUrl(

    @ManyToOne(optional = false)
    val memberReview: MemberReview,

    @Embedded
    val generationInfo: GenerationInfo,

    @Enumerated(EnumType.STRING)
    val type: UrlType,

   	val link: String,

   	@Column(name = "sort_order")
   	val order: Int
   ) : BaseEntity() {
   }