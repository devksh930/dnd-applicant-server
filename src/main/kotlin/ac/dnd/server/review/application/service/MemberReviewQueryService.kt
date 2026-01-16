package ac.dnd.server.review.application.service

import ac.dnd.server.review.domain.model.MemberReview
import ac.dnd.server.review.domain.repository.MemberReviewRepository
import ac.dnd.server.review.domain.repository.ProjectRepository
import ac.dnd.server.review.exception.FormLinkExpiredException
import ac.dnd.server.review.exception.FormLinkNotFoundException
import ac.dnd.server.review.exception.ProjectNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberReviewQueryService(
    private val memberReviewRepository: MemberReviewRepository,
    private val projectRepository: ProjectRepository
) {
    fun getMemberReviewsByLink(linkKey: String): MemberReviewProjectResult {
        val link = projectRepository.findLinkByLinkKey(linkKey)
            ?: throw FormLinkNotFoundException()

        if (link.isExpired()) {
            throw FormLinkExpiredException()
        }

        val project = projectRepository.findProjectByLinkKey(linkKey)
            ?: throw ProjectNotFoundException()

        val members = memberReviewRepository.findByGenerationAndTeamName(
            generation = project.generationInfo.generation,
            teamName = project.generationInfo.teamName
        )

        return MemberReviewProjectResult(
            generation = project.generationInfo.generation,
            teamName = project.generationInfo.teamName,
            members = members
        )
    }
}

data class MemberReviewProjectResult(
    val generation: String,
    val teamName: String,
    val members: List<MemberReview>
)
