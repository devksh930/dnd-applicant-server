package ac.dnd.server.review.infrastructure.adapter

import ac.dnd.server.review.domain.model.MemberReview
import ac.dnd.server.review.domain.model.MemberReviewProfileUrl
import ac.dnd.server.review.domain.repository.MemberReviewRepository
import ac.dnd.server.review.exception.FormLinkExpiredException
import ac.dnd.server.review.infrastructure.persistence.entity.MemberReviewProfileUrlEntity
import ac.dnd.server.review.infrastructure.persistence.mapper.MemberReviewPersistenceMapper
import ac.dnd.server.review.infrastructure.persistence.repository.MemberReviewJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.MemberReviewProfileUrlJpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
class MemberReviewRepositoryAdapter(
    private val memberReviewJpaRepository: MemberReviewJpaRepository,
    private val memberReviewProfileUrlJpaRepository: MemberReviewProfileUrlJpaRepository,
    private val memberReviewPersistenceMapper: MemberReviewPersistenceMapper
) : MemberReviewRepository {

    override fun save(memberReview: MemberReview): MemberReview {
        val entity = memberReviewJpaRepository.findById(memberReview.id).get()
        entity.update(
            name = memberReview.name,
            description = memberReview.description!!
        )
        return memberReviewPersistenceMapper.toDomain(memberReviewJpaRepository.save(entity))
    }

    override fun saveAllUrls(memberReviewId: Long, profileUrls: List<MemberReviewProfileUrl>) {
        val memberReview = memberReviewJpaRepository.findById(memberReviewId).get()
        val entities = profileUrls.map {
            MemberReviewProfileUrlEntity(
                memberReview = memberReview,
                type = it.type,
                link = it.link,
                order = it.order
            )
        }
        memberReviewProfileUrlJpaRepository.saveAll(entities)
    }

    override fun deleteUrlsByMemberReviewId(memberReviewId: Long) {
        memberReviewProfileUrlJpaRepository.deleteByMemberReviewId(memberReviewId)
    }

    override fun findMemberReview(linkKey: String, name: String): MemberReview? {
        val linkUuid = UUID.fromString(linkKey)

        val result = memberReviewJpaRepository.findMemberReviewWithLinkStatus(linkUuid, name)
            ?: return null

        if (result.getExpired() || result.getExpirationDateTime().isBefore(LocalDateTime.now())) {
            throw FormLinkExpiredException()
        }

        return result.getMemberReview()?.let { memberReviewPersistenceMapper.toDomain(it) }
    }

    override fun findByGenerationAndName(generation: String, name: String): MemberReview? {
        return memberReviewJpaRepository.findByGenerationInfoGenerationAndName(generation, name)
            ?.let { memberReviewPersistenceMapper.toDomain(it) }
    }

    override fun findByGenerationAndTeamName(generation: String, teamName: String): List<MemberReview> {
        return memberReviewJpaRepository.findByGenerationInfoGenerationAndGenerationInfoTeamName(generation, teamName)
            .map { memberReviewPersistenceMapper.toDomain(it) }
    }
}
