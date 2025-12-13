package ac.dnd.server.review.infrastructure.web.dto.request

import ac.dnd.server.review.domain.enums.UrlType
import ac.dnd.server.review.infrastructure.persistence.entity.Project
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectUrl

data class UrlLinks(
    val type: UrlType,
    val url: String,
    val order: Int? = null
) {
    fun toEntity(project: Project, defaultOrder: Int): ProjectUrl {
        return ProjectUrl(
            project = project,
            type = type,
            link = url,
            order = order ?: defaultOrder
        )
    }
}