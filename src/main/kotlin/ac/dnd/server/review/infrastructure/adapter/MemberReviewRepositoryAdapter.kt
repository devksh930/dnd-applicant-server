package ac.dnd.server.review.infrastructure.adapter

import ac.dnd.server.review.domain.repository.MemberReviewRepository
import ac.dnd.server.review.exception.FormLinkExpiredException
import ac.dnd.server.review.infrastructure.persistence.entity.MemberReviewEntity
import ac.dnd.server.review.infrastructure.persistence.entity.MemberReviewProfileUrlEntity
import ac.dnd.server.review.infrastructure.persistence.repository.FormLinkJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.MemberReviewJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.MemberReviewProfileUrlJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.ProjectJpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MemberReviewRepositoryAdapter(
    private val memberReviewJpaRepository: MemberReviewJpaRepository,
    private val memberReviewProfileUrlJpaRepository: MemberReviewProfileUrlJpaRepository,
    private val formLinkJpaRepository: FormLinkJpaRepository,
    private val projectJpaRepository: ProjectJpaRepository
) : MemberReviewRepository {

    override fun save(memberReview: MemberReviewEntity): MemberReviewEntity {
        return memberReviewJpaRepository.save(memberReview)
    }

    override fun saveAllUrls(profileUrls: List<MemberReviewProfileUrlEntity>): List<MemberReviewProfileUrlEntity> {
        return memberReviewProfileUrlJpaRepository.saveAll(profileUrls)
    }

    override fun deleteUrlsByMemberReviewId(memberReviewId: Long) {
        memberReviewProfileUrlJpaRepository.deleteByMemberReviewId(memberReviewId)
    }

    override fun findMemberReview(linkKey: String, name: String): MemberReviewEntity? {
        val formLink = formLinkJpaRepository.findByKey(UUID.fromString(linkKey))
            ?: return null

        if (formLink.isExpired()) {
            throw FormLinkExpiredException()
        }

        // targetId is projectId for MEMBER_REVIEW link
        val project = projectJpaRepository.findById(formLink.targetId).orElse(null)
            ?: return null

        return memberReviewJpaRepository.findByGenerationInfoGenerationAndGenerationInfoTeamNameAndName(
            generation = project.generationInfo.generation,
            teamName = project.generationInfo.teamName,
            name = name
        )
    }
}
