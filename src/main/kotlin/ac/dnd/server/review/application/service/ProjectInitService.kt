package ac.dnd.server.review.application.service

import ac.dnd.server.review.domain.repository.ProjectRepository
import ac.dnd.server.review.domain.enums.FormLinkType
import ac.dnd.server.review.domain.value.GenerationInfo
import ac.dnd.server.review.infrastructure.persistence.entity.FormLink
import ac.dnd.server.review.infrastructure.persistence.entity.Project
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ProjectInitService(
    private val projectRepository: ProjectRepository
) {
    @Transactional
    fun initProjects(generation: String, teamCount: Int): List<Pair<String, String>> {
        require(teamCount > 0) { "teamCount must be greater than 0" }

        val results = mutableListOf<Pair<String, String>>()

        repeat(teamCount) { idx ->
            val teamName = "${idx + 1}조"
            val project = Project(
                generationInfo = GenerationInfo(generation = generation, teamName = teamName),
                name = "$generation $teamName",
                description = ""
            )
            val saved = projectRepository.save(project)

            val key = UUID.randomUUID()
            val formLink = FormLink(
                linkType = FormLinkType.PROJECT,
                key = key,
                targetId = saved.id!!
            )
            projectRepository.saveFormLink(formLink)

            results.add(teamName to key.toString())
        }

        return results
    }
}
