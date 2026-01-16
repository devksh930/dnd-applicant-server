package ac.dnd.server.review.application.service

import ac.dnd.server.review.domain.model.FormLink
import ac.dnd.server.review.domain.repository.ProjectRepository
import ac.dnd.server.review.exception.FormLinkExpiredException
import ac.dnd.server.review.exception.FormLinkNotFoundException
import org.springframework.stereotype.Service

@Service
class LinkQueryService(
    private val projectRepository: ProjectRepository
) {
    fun getLinkInfo(linkKey: String): FormLink {
        val link = projectRepository.findLinkByLinkKey(linkKey) ?: throw FormLinkNotFoundException()

        if (link.isExpired()) throw FormLinkExpiredException()

        return link
    }

}
