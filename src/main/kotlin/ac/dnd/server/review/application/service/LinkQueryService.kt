package ac.dnd.server.review.application.service

import ac.dnd.server.review.domain.repository.ProjectRepository
import ac.dnd.server.review.exception.FormLinkExpiredException
import ac.dnd.server.review.exception.FormLinkNotFoundException
import ac.dnd.server.review.infrastructure.web.dto.response.LinkQueryResponse
import org.springframework.stereotype.Service

@Service
class LinkQueryService(
    private val projectRepository: ProjectRepository
) {
    fun getLinkInfo(linkKey: String): LinkQueryResponse {
        val link = projectRepository.findLinkByLinkKey(linkKey) ?: throw FormLinkNotFoundException()

        if (link.isExpired()) throw FormLinkExpiredException()

        return LinkQueryResponse(
            type = link.linkType,
            expiredAt = link.expirationDateTime
        )
    }

}
