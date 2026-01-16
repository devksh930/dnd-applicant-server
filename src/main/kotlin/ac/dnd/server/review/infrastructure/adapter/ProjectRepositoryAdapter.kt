package ac.dnd.server.review.infrastructure.adapter

import ac.dnd.server.review.domain.model.FormLink
import ac.dnd.server.review.domain.model.Project
import ac.dnd.server.review.domain.model.ProjectUrl
import ac.dnd.server.review.domain.repository.ProjectRepository
import ac.dnd.server.review.exception.FormLinkExpiredException
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectUrlEntity
import ac.dnd.server.review.infrastructure.persistence.mapper.ProjectPersistenceMapper
import ac.dnd.server.review.infrastructure.persistence.repository.FormLinkJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.ProjectJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.ProjectUrlJpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ProjectRepositoryAdapter(
    private val projectJpaRepository: ProjectJpaRepository,
    private val projectUrlJpaRepository: ProjectUrlJpaRepository,
    private val formLinkJpaRepository: FormLinkJpaRepository,
    private val projectPersistenceMapper: ProjectPersistenceMapper
) : ProjectRepository {

    override fun save(project: Project): Project {
        val entity = if (project.id != 0L) {
            projectJpaRepository.findById(project.id).get().apply {
                update(
                    name = project.name,
                    description = project.description,
                    techStacks = project.techStacks,
                    fileId = project.fileId
                )
            }
        } else {
            projectPersistenceMapper.toEntity(project)
        }
        return projectPersistenceMapper.toDomain(projectJpaRepository.save(entity))
    }

    override fun saveAllUrls(projectId: Long, projectUrls: List<ProjectUrl>) {
        val projectEntity = projectJpaRepository.findById(projectId).get()
        val entities = projectUrls.map {
            ProjectUrlEntity(
                project = projectEntity,
                type = it.type,
                link = it.link,
                order = it.order
            )
        }
        projectUrlJpaRepository.saveAll(entities)
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

        return projectJpaRepository.findById(formLink.targetId)
            .map { projectPersistenceMapper.toDomain(it) }
            .orElse(null)
    }

    override fun saveFormLink(formLink: FormLink): FormLink {
        val entity = projectPersistenceMapper.toEntity(formLink)
        return projectPersistenceMapper.toDomain(formLinkJpaRepository.save(entity))
    }

    override fun findLinkByLinkKey(linkKey: String): FormLink? {
        return formLinkJpaRepository.findByKey(UUID.fromString(linkKey))
            ?.let { projectPersistenceMapper.toDomain(it) }
    }
}