package ac.dnd.server.review.application.service

import ac.dnd.server.TestcontainersConfiguration
import ac.dnd.server.review.application.dto.command.ProjectCreateCommand
import ac.dnd.server.review.domain.enums.FormLinkType
import ac.dnd.server.review.domain.enums.UrlType
import ac.dnd.server.review.domain.value.GenerationInfo
import ac.dnd.server.review.domain.value.TechStacks
import ac.dnd.server.review.exception.ProjectNotFoundException
import ac.dnd.server.review.infrastructure.persistence.entity.FormLinkEntity
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectEntity
import ac.dnd.server.review.infrastructure.persistence.repository.FormLinkJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.ProjectJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.ProjectUrlJpaRepository
import ac.dnd.server.review.infrastructure.web.dto.request.UrlLinks
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
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
class ProjectCreateServiceIntegrationTest(
    private val projectCreateService: ProjectCreateService,
    private val projectJpaRepository: ProjectJpaRepository,
    private val projectUrlJpaRepository: ProjectUrlJpaRepository,
    private val formLinkJpaRepository: FormLinkJpaRepository
) : DescribeSpec({

    extensions(SpringExtension)

    beforeEach {
        projectUrlJpaRepository.deleteAll()
        formLinkJpaRepository.deleteAll()
        projectJpaRepository.deleteAll()
    }

    describe("createProject 통합 테스트") {

        context("프로젝트가 존재하고 처음 제출하는 경우") {
            it("프로젝트가 업데이트되고 true를 반환한다") {
                // given
                val project = projectJpaRepository.save(
                    ProjectEntity(
                        generationInfo = GenerationInfo("14기", "1조"),
                        name = "14기 1조",
                        description = ""
                    )
                )

                val linkKey = UUID.randomUUID()
                formLinkJpaRepository.save(
                    FormLinkEntity(
                        linkType = FormLinkType.PROJECT,
                        key = linkKey,
                        targetId = project.id!!,
                        expired = false,
                        expirationDateTime = LocalDateTime.now().plusDays(7)
                    )
                )

                val command = ProjectCreateCommand(
                    linkKey = linkKey.toString(),
                    name = "새로운 프로젝트명",
                    description = "프로젝트 설명입니다",
                    techStacks = listOf("Kotlin", "Spring Boot", "MySQL"),
                    fileId = 100L,
                    urlLinks = listOf(
                        UrlLinks(type = UrlType.GITHUB, url = "https://github.com/dnd", order = 0),
                        UrlLinks(type = UrlType.VELOG, url = "https://velog.io/dnd", order = 1)
                    )
                )

                // when
                val result = projectCreateService.createProject(command)

                // then
                result shouldBe true

                val updatedProject = projectJpaRepository.findById(project.id!!).get()
                updatedProject.name shouldBe "새로운 프로젝트명"
                updatedProject.description shouldBe "프로젝트 설명입니다"
                updatedProject.techStacks.values shouldBe listOf("Kotlin", "Spring Boot", "MySQL")
                updatedProject.fileId shouldBe 100L
                updatedProject.submittedAt.shouldNotBeNull()

                val savedUrls = projectUrlJpaRepository.findAll()
                savedUrls shouldHaveSize 2
            }
        }

        context("프로젝트를 재제출하는 경우") {
            it("기존 URL이 삭제되고 새 URL이 저장되며 false를 반환한다") {
                // given
                val project = ProjectEntity(
                    generationInfo = GenerationInfo("14기", "2조"),
                    name = "14기 2조",
                    description = "기존 설명"
                )
                // 이미 제출된 상태로 설정
                project.update(
                    name = "이전 프로젝트명",
                    description = "이전 설명",
                    techStacks = TechStacks.of(listOf("Java")),
                    fileId = 1L
                )
                val savedProject = projectJpaRepository.save(project)

                val linkKey = UUID.randomUUID()
                formLinkJpaRepository.save(
                    FormLinkEntity(
                        linkType = FormLinkType.PROJECT,
                        key = linkKey,
                        targetId = savedProject.id!!,
                        expired = false,
                        expirationDateTime = LocalDateTime.now().plusDays(7)
                    )
                )

                val command = ProjectCreateCommand(
                    linkKey = linkKey.toString(),
                    name = "수정된 프로젝트명",
                    description = "수정된 설명",
                    techStacks = listOf("React", "TypeScript"),
                    fileId = 200L,
                    urlLinks = listOf(
                        UrlLinks(type = UrlType.LINKEDIN, url = "https://linkedin.com/in/test", order = 0)
                    )
                )

                // when
                val result = projectCreateService.createProject(command)

                // then
                result shouldBe false

                val updatedProject = projectJpaRepository.findById(savedProject.id!!).get()
                updatedProject.name shouldBe "수정된 프로젝트명"
                updatedProject.techStacks.values shouldBe listOf("React", "TypeScript")
                updatedProject.fileId shouldBe 200L

                val savedUrls = projectUrlJpaRepository.findAll()
                savedUrls shouldHaveSize 1
                savedUrls[0].type shouldBe UrlType.LINKEDIN
            }
        }

        context("존재하지 않는 링크로 요청하는 경우") {
            it("ProjectNotFoundException이 발생한다") {
                // given
                val invalidLinkKey = UUID.randomUUID().toString()
                val command = ProjectCreateCommand(
                    linkKey = invalidLinkKey,
                    name = "프로젝트명",
                    description = "설명",
                    techStacks = null,
                    fileId = null,
                    urlLinks = null
                )

                // when & then
                shouldThrow<ProjectNotFoundException> {
                    projectCreateService.createProject(command)
                }
            }
        }

        context("urlLinks가 null인 경우") {
            it("URL 저장 없이 프로젝트만 업데이트된다") {
                // given
                val project = projectJpaRepository.save(
                    ProjectEntity(
                        generationInfo = GenerationInfo("14기", "3조"),
                        name = "14기 3조",
                        description = ""
                    )
                )

                val linkKey = UUID.randomUUID()
                formLinkJpaRepository.save(
                    FormLinkEntity(
                        linkType = FormLinkType.PROJECT,
                        key = linkKey,
                        targetId = project.id!!,
                        expired = false,
                        expirationDateTime = LocalDateTime.now().plusDays(7)
                    )
                )

                val command = ProjectCreateCommand(
                    linkKey = linkKey.toString(),
                    name = "URL 없는 프로젝트",
                    description = "설명만 있음",
                    techStacks = listOf("Kotlin"),
                    fileId = null,
                    urlLinks = null
                )

                // when
                projectCreateService.createProject(command)

                // then
                val updatedProject = projectJpaRepository.findById(project.id!!).get()
                updatedProject.name shouldBe "URL 없는 프로젝트"

                val savedUrls = projectUrlJpaRepository.findAll()
                savedUrls shouldHaveSize 0
            }
        }

        context("techStacks가 빈 리스트인 경우") {
            it("빈 TechStacks로 저장된다") {
                // given
                val project = projectJpaRepository.save(
                    ProjectEntity(
                        generationInfo = GenerationInfo("14기", "4조"),
                        name = "14기 4조",
                        description = "",
                        techStacks = TechStacks.of(listOf("기존 스택"))
                    )
                )

                val linkKey = UUID.randomUUID()
                formLinkJpaRepository.save(
                    FormLinkEntity(
                        linkType = FormLinkType.PROJECT,
                        key = linkKey,
                        targetId = project.id!!,
                        expired = false,
                        expirationDateTime = LocalDateTime.now().plusDays(7)
                    )
                )

                val command = ProjectCreateCommand(
                    linkKey = linkKey.toString(),
                    name = "스택 없는 프로젝트",
                    description = "설명",
                    techStacks = emptyList(),
                    fileId = null,
                    urlLinks = null
                )

                // when
                projectCreateService.createProject(command)

                // then
                val updatedProject = projectJpaRepository.findById(project.id!!).get()
                updatedProject.techStacks.values shouldBe emptyList()
            }
        }
    }
})