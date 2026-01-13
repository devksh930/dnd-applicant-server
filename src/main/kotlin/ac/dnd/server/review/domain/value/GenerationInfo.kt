package ac.dnd.server.review.domain.value

import jakarta.persistence.Embeddable

@Embeddable
class GenerationInfo(
    //기수
    val generation: String,
    //조
    val teamName: String
)