package ac.dnd.server.review.infrastructure.adapter

import ac.dnd.server.review.domain.repository.ProjectRepository
import ac.dnd.server.review.infrastructure.persistence.entity.Project
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectUrl
import ac.dnd.server.review.infrastructure.persistence.repository.FormLinkJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.ProjectJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.ProjectUrlJpaRepository
import org.springframework.stereotype.Repository
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

    override fun findProjectByLinkKey(linkKey: String): Project? {
        return formLinkJpaRepository.findByKey(UUID.fromString(linkKey))
            ?.let { projectJpaRepository.findById(it.targetId).orElse(null) }
    }
}