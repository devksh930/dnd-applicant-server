package ac.dnd.server.review.application.service

import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.review.application.dto.command.ProjectCreateCommand
import ac.dnd.server.review.domain.enums.UrlType
import ac.dnd.server.review.domain.repository.ProjectRepository
import ac.dnd.server.review.domain.value.GenerationInfo
import ac.dnd.server.review.domain.value.TechStacks
import ac.dnd.server.review.exception.ProjectNotFoundException
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectEntity
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectUrlEntity
import ac.dnd.server.review.infrastructure.web.dto.request.UrlLinks
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

@UnitTest
class ProjectCreateServiceTest : DescribeSpec({

    lateinit var projectRepository: ProjectRepository
    lateinit var projectCreateService: ProjectCreateService

    beforeEach {
        projectRepository = mock<ProjectRepository>()
        projectCreateService = ProjectCreateService(projectRepository)
    }

    describe("createProject") {

        context("프로젝트가 존재하고 처음 제출하는 경우") {
            it("프로젝트를 업데이트하고 true를 반환한다") {
                // given
                val linkKey = "test-link-key"
                val project = ProjectEntity(
                    generationInfo = GenerationInfo("14기", "1조"),
                    name = "14기 1조",
                    description = ""
                ).apply {
                    val idField = ProjectEntity::class.java.superclass.getDeclaredField("id")
                    idField.isAccessible = true
                    idField.set(this, 1L)
                }

                val command = ProjectCreateCommand(
                    linkKey = linkKey,
                    name = "새로운 프로젝트명",
                    description = "프로젝트 설명",
                    techStacks = listOf("Kotlin", "Spring"),
                    fileId = 100L,
                    urlLinks = listOf(
                        UrlLinks(type = UrlType.GITHUB, url = "https://github.com/test", order = 0)
                    )
                )

                whenever(projectRepository.findProjectByLinkKey(linkKey)).thenReturn(project)
                doNothing().whenever(projectRepository).deleteUrlsByProjectId(1L)
                whenever(projectRepository.saveAllUrls(any())).thenReturn(emptyList())

                // when
                val result = projectCreateService.createProject(command)

                // then
                result shouldBe true
                project.name shouldBe "새로운 프로젝트명"
                project.description shouldBe "프로젝트 설명"
                project.techStacks shouldBe TechStacks.of(listOf("Kotlin", "Spring"))
                project.fileId shouldBe 100L

                verify(projectRepository).deleteUrlsByProjectId(1L)
                verify(projectRepository).saveAllUrls(any())
            }
        }

        context("프로젝트가 이미 제출된 적 있는 경우") {
            it("프로젝트를 업데이트하고 false를 반환한다") {
                // given
                val linkKey = "test-link-key"
                val project = ProjectEntity(
                    generationInfo = GenerationInfo("14기", "1조"),
                    name = "14기 1조",
                    description = "기존 설명"
                ).apply {
                    val idField = ProjectEntity::class.java.superclass.getDeclaredField("id")
                    idField.isAccessible = true
                    idField.set(this, 1L)
                    // 이미 제출된 상태로 설정
                    val submittedAtField = ProjectEntity::class.java.getDeclaredField("submittedAt")
                    submittedAtField.isAccessible = true
                    submittedAtField.set(this, LocalDateTime.now().minusDays(1))
                }

                val command = ProjectCreateCommand(
                    linkKey = linkKey,
                    name = "수정된 프로젝트명",
                    description = "수정된 설명",
                    techStacks = listOf("Java"),
                    fileId = null,
                    urlLinks = null
                )

                whenever(projectRepository.findProjectByLinkKey(linkKey)).thenReturn(project)
                doNothing().whenever(projectRepository).deleteUrlsByProjectId(1L)

                // when
                val result = projectCreateService.createProject(command)

                // then
                result shouldBe false
                project.name shouldBe "수정된 프로젝트명"
                project.description shouldBe "수정된 설명"

                verify(projectRepository).deleteUrlsByProjectId(1L)
                verify(projectRepository, never()).saveAllUrls(any())
            }
        }

        context("프로젝트가 존재하지 않는 경우") {
            it("ProjectNotFoundException이 발생한다") {
                // given
                val linkKey = "invalid-link-key"
                val command = ProjectCreateCommand(
                    linkKey = linkKey,
                    name = "프로젝트명",
                    description = "설명",
                    techStacks = null,
                    fileId = null,
                    urlLinks = null
                )

                whenever(projectRepository.findProjectByLinkKey(linkKey)).thenReturn(null)

                // when & then
                shouldThrow<ProjectNotFoundException> {
                    projectCreateService.createProject(command)
                }
            }
        }

        context("urlLinks가 여러 개인 경우") {
            it("모든 URL을 저장한다") {
                // given
                val linkKey = "test-link-key"
                val project = ProjectEntity(
                    generationInfo = GenerationInfo("14기", "2조"),
                    name = "14기 2조",
                    description = ""
                ).apply {
                    val idField = ProjectEntity::class.java.superclass.getDeclaredField("id")
                    idField.isAccessible = true
                    idField.set(this, 2L)
                }

                val command = ProjectCreateCommand(
                    linkKey = linkKey,
                    name = "프로젝트명",
                    description = "설명",
                    techStacks = listOf("React", "TypeScript"),
                    fileId = null,
                    urlLinks = listOf(
                        UrlLinks(type = UrlType.GITHUB, url = "https://github.com/test", order = 0),
                        UrlLinks(type = UrlType.VELOG, url = "https://velog.io/test", order = 1),
                        UrlLinks(type = UrlType.LINKEDIN, url = "https://linkedin.com/in/test", order = null)
                    )
                )

                whenever(projectRepository.findProjectByLinkKey(linkKey)).thenReturn(project)
                doNothing().whenever(projectRepository).deleteUrlsByProjectId(2L)

                val urlCaptor = argumentCaptor<List<ProjectUrlEntity>>()
                whenever(projectRepository.saveAllUrls(urlCaptor.capture())).thenReturn(emptyList())

                // when
                projectCreateService.createProject(command)

                // then
                val savedUrls = urlCaptor.firstValue
                savedUrls.size shouldBe 3
                savedUrls[0].type shouldBe UrlType.GITHUB
                savedUrls[0].order shouldBe 0
                savedUrls[1].type shouldBe UrlType.VELOG
                savedUrls[1].order shouldBe 1
                savedUrls[2].type shouldBe UrlType.LINKEDIN
                savedUrls[2].order shouldBe 2 // order가 null이면 index로 설정
            }
        }

        context("techStacks가 null인 경우") {
            it("빈 TechStacks로 업데이트된다") {
                // given
                val linkKey = "test-link-key"
                val project = ProjectEntity(
                    generationInfo = GenerationInfo("14기", "3조"),
                    name = "14기 3조",
                    description = "",
                    techStacks = TechStacks.of(listOf("기존 스택"))
                ).apply {
                    val idField = ProjectEntity::class.java.superclass.getDeclaredField("id")
                    idField.isAccessible = true
                    idField.set(this, 3L)
                }

                val command = ProjectCreateCommand(
                    linkKey = linkKey,
                    name = "프로젝트명",
                    description = "설명",
                    techStacks = null,
                    fileId = null,
                    urlLinks = null
                )

                whenever(projectRepository.findProjectByLinkKey(linkKey)).thenReturn(project)
                doNothing().whenever(projectRepository).deleteUrlsByProjectId(3L)

                // when
                projectCreateService.createProject(command)

                // then
                project.techStacks.values shouldBe emptyList()
            }
        }
    }
})