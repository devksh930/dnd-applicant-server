package ac.dnd.server.review.application.service

import ac.dnd.server.TestcontainersConfiguration
import ac.dnd.server.review.domain.enums.FormLinkType
import ac.dnd.server.review.exception.FormLinkExpiredException
import ac.dnd.server.review.exception.FormLinkNotFoundException
import ac.dnd.server.review.infrastructure.persistence.entity.FormLinkEntity
import ac.dnd.server.review.infrastructure.persistence.repository.FormLinkJpaRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@SpringBootTest
@Import(TestcontainersConfiguration::class)
@Transactional
@ActiveProfiles("test")
class LinkQueryServiceIntegrationTest(
    private val linkQueryService: LinkQueryService,
    private val formLinkJpaRepository: FormLinkJpaRepository
) : DescribeSpec({

    extensions(SpringExtension)

    beforeEach {
        formLinkJpaRepository.deleteAll()
    }

    describe("getLinkInfo 통합 테스트") {

        context("유효한 링크가 DB에 존재하는 경우") {
            it("FormLink를 정상적으로 반환한다") {
                // given
                val linkKey = UUID.randomUUID()
                val expirationDateTime = LocalDateTime.now().plusDays(7)
                val formLink = FormLinkEntity(
                    linkType = FormLinkType.PROJECT,
                    key = linkKey,
                    targetId = 1L,
                    expired = false,
                    expirationDateTime = expirationDateTime
                )
                formLinkJpaRepository.save(formLink)

                // when
                val result = linkQueryService.getLinkInfo(linkKey.toString())

                // then
                result.linkType shouldBe FormLinkType.PROJECT
            }
        }

        context("링크가 DB에 존재하지 않는 경우") {
            it("FormLinkNotFoundException이 발생한다") {
                // given
                val linkKey = UUID.randomUUID().toString()

                // when & then
                shouldThrow<FormLinkNotFoundException> {
                    linkQueryService.getLinkInfo(linkKey)
                }
            }
        }

        context("DB에 저장된 링크가 만료된 경우") {
            it("FormLinkExpiredException이 발생한다") {
                // given
                val linkKey = UUID.randomUUID()
                val formLink = FormLinkEntity(
                    linkType = FormLinkType.PROJECT,
                    key = linkKey,
                    targetId = 1L,
                    expired = true,
                    expirationDateTime = LocalDateTime.now().plusDays(7)
                )
                formLinkJpaRepository.save(formLink)

                // when & then
                shouldThrow<FormLinkExpiredException> {
                    linkQueryService.getLinkInfo(linkKey.toString())
                }
            }
        }

        context("DB에 저장된 링크의 만료일시가 지난 경우") {
            it("FormLinkExpiredException이 발생한다") {
                // given
                val linkKey = UUID.randomUUID()
                val formLink = FormLinkEntity(
                    linkType = FormLinkType.PROJECT,
                    key = linkKey,
                    targetId = 1L,
                    expired = false,
                    expirationDateTime = LocalDateTime.now().minusDays(1)
                )
                formLinkJpaRepository.save(formLink)

                // when & then
                shouldThrow<FormLinkExpiredException> {
                    linkQueryService.getLinkInfo(linkKey.toString())
                }
            }
        }

        context("MEMBER_REVIEW 타입의 링크가 저장된 경우") {
            it("해당 타입으로 조회된다") {
                // given
                val linkKey = UUID.randomUUID()
                val formLink = FormLinkEntity(
                    linkType = FormLinkType.MEMBER_REVIEW,
                    key = linkKey,
                    targetId = 2L,
                    expired = false,
                    expirationDateTime = LocalDateTime.now().plusDays(14)
                )
                formLinkJpaRepository.save(formLink)

                // when
                val result = linkQueryService.getLinkInfo(linkKey.toString())

                // then
                result.linkType shouldBe FormLinkType.MEMBER_REVIEW
            }
        }
    }
})
