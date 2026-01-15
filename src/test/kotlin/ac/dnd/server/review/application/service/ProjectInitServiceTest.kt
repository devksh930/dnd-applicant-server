package ac.dnd.server.review.application.service

import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.review.domain.enums.FormLinkType
import ac.dnd.server.review.domain.repository.ProjectRepository
import ac.dnd.server.review.exception.InvalidTeamCountException
import ac.dnd.server.review.infrastructure.persistence.entity.FormLinkEntity
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectEntity
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@UnitTest
class ProjectInitServiceTest : DescribeSpec({

    lateinit var projectRepository: ProjectRepository
    lateinit var projectInitService: ProjectInitService

    beforeEach {
        projectRepository = mock<ProjectRepository>()
        projectInitService = ProjectInitService(projectRepository)
    }

    describe("initProjects") {

        context("유효한 generation과 teamCount가 주어진 경우") {
            it("지정된 수만큼 프로젝트와 링크를 생성하고 결과를 반환한다") {
                // given
                val generation = "14기"
                val teamCount = 3

                val projectCaptor = argumentCaptor<ProjectEntity>()
                val formLinkCaptor = argumentCaptor<FormLinkEntity>()

                var projectIdCounter = 1L
                whenever(projectRepository.save(any<ProjectEntity>())).thenAnswer { invocation ->
                    val project = invocation.getArgument<ProjectEntity>(0)
                    val idField = ProjectEntity::class.java.superclass.getDeclaredField("id")
                    idField.isAccessible = true
                    idField.set(project, projectIdCounter++)
                    project
                }

                whenever(projectRepository.saveFormLink(any<FormLinkEntity>())).thenAnswer { invocation ->
                    invocation.getArgument<FormLinkEntity>(0)
                }

                // when
                val result = projectInitService.initProjects(generation, teamCount)

                // then
                result shouldHaveSize 3
                result[0].first shouldBe "1조"
                result[1].first shouldBe "2조"
                result[2].first shouldBe "3조"

                // UUID 형식 검증
                result.forEach { (_, projectKey, reviewKey) ->
                    projectKey shouldMatch "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"
                    reviewKey shouldMatch "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"
                }

                verify(projectRepository, times(3)).save(projectCaptor.capture())
                verify(projectRepository, times(6)).saveFormLink(formLinkCaptor.capture())

                val savedProjects = projectCaptor.allValues
                savedProjects[0].generationInfo.generation shouldBe "14기"
                savedProjects[0].generationInfo.teamName shouldBe "1조"
                savedProjects[0].name shouldBe "14기 1조"
                savedProjects[1].generationInfo.teamName shouldBe "2조"
                savedProjects[2].generationInfo.teamName shouldBe "3조"

                val savedFormLinks = formLinkCaptor.allValues
                savedFormLinks shouldHaveSize 6
                savedFormLinks.filter { it.linkType == FormLinkType.PROJECT } shouldHaveSize 3
                savedFormLinks.filter { it.linkType == FormLinkType.MEMBER_REVIEW } shouldHaveSize 3
            }
        }

        context("teamCount가 1인 경우") {
            it("하나의 프로젝트만 생성한다") {
                // given
                val generation = "15기"
                val teamCount = 1

                whenever(projectRepository.save(any<ProjectEntity>())).thenAnswer { invocation ->
                    val project = invocation.getArgument<ProjectEntity>(0)
                    val idField = ProjectEntity::class.java.superclass.getDeclaredField("id")
                    idField.isAccessible = true
                    idField.set(project, 1L)
                    project
                }
                whenever(projectRepository.saveFormLink(any<FormLinkEntity>())).thenAnswer { invocation ->
                    invocation.getArgument<FormLinkEntity>(0)
                }

                // when
                val result = projectInitService.initProjects(generation, teamCount)

                // then
                result shouldHaveSize 1
                result[0].first shouldBe "1조"

                verify(projectRepository, times(1)).save(any())
                verify(projectRepository, times(2)).saveFormLink(any())
            }
        }

        context("teamCount가 0인 경우") {
            it("InvalidTeamCountException이 발생한다") {
                // given
                val generation = "14기"
                val teamCount = 0

                // when & then
                shouldThrow<InvalidTeamCountException> {
                    projectInitService.initProjects(generation, teamCount)
                }
            }
        }

        context("teamCount가 음수인 경우") {
            it("InvalidTeamCountException이 발생한다") {
                // given
                val generation = "14기"
                val teamCount = -1

                // when & then
                shouldThrow<InvalidTeamCountException> {
                    projectInitService.initProjects(generation, teamCount)
                }
            }
        }

        context("다양한 기수로 프로젝트를 생성하는 경우") {
            it("기수 정보가 올바르게 설정된다") {
                // given
                val generation = "DND 14기"
                val teamCount = 2

                val projectCaptor = argumentCaptor<ProjectEntity>()

                whenever(projectRepository.save(projectCaptor.capture())).thenAnswer { invocation ->
                    val project = invocation.getArgument<ProjectEntity>(0)
                    val idField = ProjectEntity::class.java.superclass.getDeclaredField("id")
                    idField.isAccessible = true
                    idField.set(project, 1L)
                    project
                }
                whenever(projectRepository.saveFormLink(any<FormLinkEntity>())).thenAnswer { invocation ->
                    invocation.getArgument<FormLinkEntity>(0)
                }

                // when
                projectInitService.initProjects(generation, teamCount)

                // then
                val savedProjects = projectCaptor.allValues
                savedProjects.forEach { project ->
                    project.generationInfo.generation shouldBe "DND 14기"
                }
                savedProjects[0].name shouldBe "DND 14기 1조"
                savedProjects[1].name shouldBe "DND 14기 2조"
            }
        }

        context("많은 수의 팀을 생성하는 경우") {
            it("모든 팀이 순서대로 생성된다") {
                // given
                val generation = "14기"
                val teamCount = 10

                var projectIdCounter = 1L
                whenever(projectRepository.save(any<ProjectEntity>())).thenAnswer { invocation ->
                    val project = invocation.getArgument<ProjectEntity>(0)
                    val idField = ProjectEntity::class.java.superclass.getDeclaredField("id")
                    idField.isAccessible = true
                    idField.set(project, projectIdCounter++)
                    project
                }
                whenever(projectRepository.saveFormLink(any<FormLinkEntity>())).thenAnswer { invocation ->
                    invocation.getArgument<FormLinkEntity>(0)
                }

                // when
                val result = projectInitService.initProjects(generation, teamCount)

                // then
                result shouldHaveSize 10
                result.forEachIndexed { index, (teamName, _, _) ->
                    teamName shouldBe "${index + 1}조"
                }
            }
        }
    }
})