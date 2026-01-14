package ac.dnd.server.review.application.service

import ac.dnd.server.review.application.dto.command.ProjectCreateCommand
import ac.dnd.server.review.domain.repository.ProjectRepository
import ac.dnd.server.review.domain.value.TechStacks
import ac.dnd.server.review.exception.ProjectNotFoundException
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectUrlEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProjectCreateService(
    private val projectRepository: ProjectRepository
) {
    @Transactional
    fun createProject(command: ProjectCreateCommand): Boolean {
        val project = projectRepository.findProjectByLinkKey(command.linkKey)
            ?: throw ProjectNotFoundException()

        val isFirstSubmission = project.isFirstSubmission()

        project.update(
            name = command.name,
            description = command.description,
            techStacks = TechStacks.of(command.techStacks),
            fileId = command.fileId
        )

        projectRepository.deleteUrlsByProjectId(project.id!!)
        command.urlLinks?.let { urlLinks ->
            val projectUrls = urlLinks.mapIndexed { index, urlLink ->
                ProjectUrlEntity(
                    project = project,
                    type = urlLink.type,
                    link = urlLink.url,
                    order = urlLink.order ?: index
                )
            }
            projectRepository.saveAllUrls(projectUrls)
        }

        return isFirstSubmission
    }
}
