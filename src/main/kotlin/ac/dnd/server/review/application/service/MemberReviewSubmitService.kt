package ac.dnd.server.review.application.service

import ac.dnd.server.review.application.dto.command.MemberReviewSubmitCommand
import ac.dnd.server.review.domain.model.MemberReviewProfileUrl
import ac.dnd.server.review.domain.repository.MemberReviewRepository
import ac.dnd.server.review.exception.MemberReviewNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberReviewSubmitService(
    private val memberReviewRepository: MemberReviewRepository
) {
    @Transactional
    fun submitMemberReview(command: MemberReviewSubmitCommand): Boolean {
        val memberReview = memberReviewRepository.findMemberReview(command.linkKey, command.name)
            ?: throw MemberReviewNotFoundException()

        val isFirstSubmission = memberReview.isFirstSubmission()

        memberReview.update(
            name = command.name,
            description = command.description
        )
        memberReviewRepository.save(memberReview)

        memberReviewRepository.deleteUrlsByMemberReviewId(memberReview.id)
        command.urlLinks?.let { urlLinks ->
            val profileUrls = urlLinks.mapIndexed { index, urlLink ->
                MemberReviewProfileUrl(
                    type = urlLink.type,
                    link = urlLink.url,
                    order = urlLink.order ?: index
                )
            }
            memberReviewRepository.saveAllUrls(memberReview.id, profileUrls)
        }

        return isFirstSubmission
    }
}
