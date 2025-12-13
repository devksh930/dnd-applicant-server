package ac.dnd.server.review.application.service

import ac.dnd.server.review.application.dto.command.ProjectCreateCommand
import ac.dnd.server.review.domain.repository.ProjectRepository
import ac.dnd.server.review.infrastructure.persistence.entity.Project
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProjectCreateService(
    private val projectRepository: ProjectRepository
) {
    @Transactional
    fun createProject(command: ProjectCreateCommand) {
        val project = projectRepository.findProjectByLinkKey(command.linkKey)

        val putProject = Project(
            generationInfo = project!!.generationInfo,
            name = command.name,
            description = command.description,
            techStack = command.techStack?.joinToString(",") ?: ""
        )

        command.urlLinks?.let { urlLinks ->
            val projectUrls = urlLinks.mapIndexed { index, urlLink ->
                urlLink.toEntity(putProject, index)
            }

            projectRepository.saveAllUrls(projectUrls)
        }
    }

}
