package ac.dnd.server.review.application.service

import ac.dnd.server.review.domain.enums.FormLinkType
import ac.dnd.server.review.domain.repository.ProjectRepository
import ac.dnd.server.review.domain.value.GenerationInfo
import ac.dnd.server.review.exception.InvalidTeamCountException
import ac.dnd.server.review.infrastructure.persistence.entity.FormLinkEntity
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ProjectInitService(
    private val projectRepository: ProjectRepository
) {
    @Transactional
    fun initProjects(generation: String, teamCount: Int): List<Triple<String, String, String>> {
        if (teamCount <= 0) throw InvalidTeamCountException()

        val results = mutableListOf<Triple<String, String, String>>()

        repeat(teamCount) { idx ->
            val teamName = "${idx + 1}조"
            val project = ProjectEntity(
                generationInfo = GenerationInfo(generation = generation, teamName = teamName),
                name = "$generation $teamName",
                description = ""
            )
            val saved = projectRepository.save(project)

            val projectLinkKey = UUID.randomUUID()
            val projectLink = FormLinkEntity(
                linkType = FormLinkType.PROJECT,
                key = projectLinkKey,
                targetId = saved.id!!
            )
            projectRepository.saveFormLink(projectLink)

            val reviewLinkKey = UUID.randomUUID()
            val reviewLink = FormLinkEntity(
                linkType = FormLinkType.MEMBER_REVIEW,
                key = reviewLinkKey,
                targetId = saved.id!!
            )
            projectRepository.saveFormLink(reviewLink)

            results.add(Triple(teamName, projectLinkKey.toString(), reviewLinkKey.toString()))
        }

        return results
    }
}
