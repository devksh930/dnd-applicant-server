package ac.dnd.server.review.application.service

import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.review.domain.enums.FormLinkType
import ac.dnd.server.review.domain.repository.ProjectRepository
import ac.dnd.server.review.exception.FormLinkExpiredException
import ac.dnd.server.review.exception.FormLinkNotFoundException
import ac.dnd.server.review.infrastructure.persistence.entity.FormLink
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.UUID

@UnitTest
class LinkQueryServiceTest : DescribeSpec({

    val projectRepository = mock<ProjectRepository>()
    val linkQueryService = LinkQueryService(projectRepository)

    describe("getLinkInfo") {

        context("링크가 존재하고 유효한 경우") {
            it("LinkQueryResponse를 반환한다") {
                // given
                val linkKey = UUID.randomUUID()
                val expirationDateTime = LocalDateTime.now().plusDays(7)
                val formLink = FormLink(
                    linkType = FormLinkType.PROJECT,
                    key = linkKey,
                    targetId = 1L,
                    expired = false,
                    expirationDateTime = expirationDateTime
                )

                whenever(projectRepository.findLinkByLinkKey(linkKey.toString())).thenReturn(formLink)

                // when
                val result = linkQueryService.getLinkInfo(linkKey.toString())

                // then
                result.type shouldBe FormLinkType.PROJECT
                result.expiredAt shouldBe expirationDateTime
            }
        }

        context("링크가 존재하지 않는 경우") {
            it("FormLinkNotFoundException이 발생한다") {
                // given
                val linkKey = UUID.randomUUID().toString()

                whenever(projectRepository.findLinkByLinkKey(linkKey)).thenReturn(null)

                // when & then
                shouldThrow<FormLinkNotFoundException> {
                    linkQueryService.getLinkInfo(linkKey)
                }
            }
        }

        context("링크가 expired 플래그로 만료된 경우") {
            it("FormLinkExpiredException이 발생한다") {
                // given
                val linkKey = UUID.randomUUID()
                val formLink = FormLink(
                    linkType = FormLinkType.PROJECT,
                    key = linkKey,
                    targetId = 1L,
                    expired = true,
                    expirationDateTime = LocalDateTime.now().plusDays(7)
                )

                whenever(projectRepository.findLinkByLinkKey(linkKey.toString())).thenReturn(formLink)

                // when & then
                shouldThrow<FormLinkExpiredException> {
                    linkQueryService.getLinkInfo(linkKey.toString())
                }
            }
        }

        context("링크가 만료일시를 초과한 경우") {
            it("FormLinkExpiredException이 발생한다") {
                // given
                val linkKey = UUID.randomUUID()
                val formLink = FormLink(
                    linkType = FormLinkType.PROJECT,
                    key = linkKey,
                    targetId = 1L,
                    expired = false,
                    expirationDateTime = LocalDateTime.now().minusDays(1)
                )

                whenever(projectRepository.findLinkByLinkKey(linkKey.toString())).thenReturn(formLink)

                // when & then
                shouldThrow<FormLinkExpiredException> {
                    linkQueryService.getLinkInfo(linkKey.toString())
                }
            }
        }

        context("MEMBER_REVIEW 타입의 링크인 경우") {
            it("해당 타입의 LinkQueryResponse를 반환한다") {
                // given
                val linkKey = UUID.randomUUID()
                val expirationDateTime = LocalDateTime.now().plusDays(14)
                val formLink = FormLink(
                    linkType = FormLinkType.MEMBER_REVIEW,
                    key = linkKey,
                    targetId = 2L,
                    expired = false,
                    expirationDateTime = expirationDateTime
                )

                whenever(projectRepository.findLinkByLinkKey(linkKey.toString())).thenReturn(formLink)

                // when
                val result = linkQueryService.getLinkInfo(linkKey.toString())

                // then
                result.type shouldBe FormLinkType.MEMBER_REVIEW
                result.expiredAt shouldBe expirationDateTime
            }
        }
    }
})