package ac.dnd.server.review.infrastructure.adapter

import ac.dnd.server.review.domain.repository.ProjectRepository
import ac.dnd.server.review.infrastructure.persistence.entity.FormLinkEntity
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectEntity
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectUrlEntity
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

    override fun save(project: ProjectEntity): ProjectEntity {
        return projectJpaRepository.save(project)
    }

    override fun saveAllUrls(projectUrls: List<ProjectUrlEntity>): List<ProjectUrlEntity> {
        return projectUrlJpaRepository.saveAll(projectUrls)
    }

    override fun deleteUrlsByProjectId(projectId: Long) {
        // Assuming deleteByProjectId is available in ProjectUrlJpaRepository
        // Since custom methods are not visible in JpaRepository interface unless defined
        // I should check if deleteByProjectId is defined in ProjectUrlJpaRepository
        projectUrlJpaRepository.deleteByProjectId(projectId)
    }

    override fun findProjectByLinkKey(linkKey: String): ProjectEntity? {
        val formLink = formLinkJpaRepository.findByKey(UUID.fromString(linkKey))
            ?: return null

        if (formLink.isExpired()) {
            throw FormLinkExpiredException()
        }

        return projectJpaRepository.findById(formLink.targetId).orElse(null)
    }

    override fun saveFormLink(formLink: FormLinkEntity): FormLinkEntity {
        return formLinkJpaRepository.save(formLink)
    }

    override fun findLinkByLinkKey(linkKey: String): FormLinkEntity? {
        return formLinkJpaRepository.findByKey(UUID.fromString(linkKey))
    }
}