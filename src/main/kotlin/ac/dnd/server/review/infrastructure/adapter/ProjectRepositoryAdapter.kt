package ac.dnd.server.review.infrastructure.adapter

import ac.dnd.server.review.domain.repository.ProjectRepository
import ac.dnd.server.review.infrastructure.persistence.entity.FormLink
import ac.dnd.server.review.infrastructure.persistence.entity.Project
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectUrl
import ac.dnd.server.review.infrastructure.persistence.repository.FormLinkJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.ProjectJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.ProjectUrlJpaRepository
import org.springframework.stereotype.Repository
import ac.dnd.server.review.exception.FormLinkExpiredException
import java.util.UUID

@Repository
class ProjectRepositoryAdapter(
    private val projectJpaRepository: ProjectJpaRepository,
    private val projectUrlJpaRepository: ProjectUrlJpaRepository,
    private val formLinkJpaRepository: FormLinkJpaRepository
) : ProjectRepository {

    override fun save(project: Project): Project {
        return projectJpaRepository.save(project)
    }

    override fun saveAllUrls(projectUrls: List<ProjectUrl>): List<ProjectUrl> {
        return projectUrlJpaRepository.saveAll(projectUrls)
    }

    override fun deleteUrlsByProjectId(projectId: Long) {
        projectUrlJpaRepository.deleteByProjectId(projectId)
    }

    override fun findProjectByLinkKey(linkKey: String): Project? {
        val formLink = formLinkJpaRepository.findByKey(UUID.fromString(linkKey))
            ?: return null

        if (formLink.isExpired()) {
            throw FormLinkExpiredException()
        }

        return projectJpaRepository.findById(formLink.targetId).orElse(null)
    }

    override fun saveFormLink(formLink: FormLink): FormLink {
        return formLinkJpaRepository.save(formLink)
    }

    override fun findLinkByLinkKey(linkKey: String): FormLink? {
        return formLinkJpaRepository.findByKey(UUID.fromString(linkKey))
    }
}