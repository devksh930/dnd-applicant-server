package ac.dnd.server.review.application.service

import ac.dnd.server.TestcontainersConfiguration
import ac.dnd.server.review.domain.enums.FormLinkType
import ac.dnd.server.review.exception.InvalidTeamCountException
import ac.dnd.server.review.infrastructure.persistence.repository.FormLinkJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.ProjectJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.ProjectUrlJpaRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
@Import(TestcontainersConfiguration::class)
@Transactional
@ActiveProfiles("test")
class ProjectInitServiceIntegrationTest(
    private val projectInitService: ProjectInitService,
    private val projectJpaRepository: ProjectJpaRepository,
    private val formLinkJpaRepository: FormLinkJpaRepository,
    private val projectUrlJpaRepository: ProjectUrlJpaRepository
) : DescribeSpec({

    extensions(SpringExtension)

    beforeEach {
        projectUrlJpaRepository.deleteAll()
        formLinkJpaRepository.deleteAll()
        projectJpaRepository.deleteAll()
    }

    describe("initProjects 통합 테스트") {

        context("유효한 인자로 프로젝트를 초기화하는 경우") {
            it("프로젝트와 FormLink가 DB에 저장된다") {
                // given
                val generation = "14기"
                val teamCount = 3

                // when
                val result = projectInitService.initProjects(generation, teamCount)

                // then
                result shouldHaveSize 3
                result[0].first shouldBe "1조"
                result[1].first shouldBe "2조"
                result[2].first shouldBe "3조"

                // DB에 프로젝트가 저장되었는지 확인
                val savedProjects = projectJpaRepository.findAll()
                savedProjects shouldHaveSize 3

                savedProjects.forEach { project ->
                    project.generationInfo.generation shouldBe "14기"
                    project.description shouldBe ""
                }

                savedProjects[0].generationInfo.teamName shouldBe "1조"
                savedProjects[0].name shouldBe "14기 1조"
                savedProjects[1].generationInfo.teamName shouldBe "2조"
                savedProjects[2].generationInfo.teamName shouldBe "3조"

                // DB에 FormLink가 저장되었는지 확인
                val savedFormLinks = formLinkJpaRepository.findAll()
                savedFormLinks shouldHaveSize 6 // 3 teams * 2 links

                savedFormLinks.filter { it.linkType == FormLinkType.PROJECT } shouldHaveSize 3
                savedFormLinks.filter { it.linkType == FormLinkType.MEMBER_REVIEW } shouldHaveSize 3
            }
        }

        context("생성된 링크 키가 유효한 UUID인지 확인") {
            it("반환된 키로 FormLink를 조회할 수 있다") {
                // given
                val generation = "15기"
                val teamCount = 2

                // when
                val result = projectInitService.initProjects(generation, teamCount)

                // then
                result.forEach { (teamName, projectKeyString, reviewKeyString) ->
                    // UUID 형식 검증
                    projectKeyString shouldMatch "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"
                    reviewKeyString shouldMatch "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"

                    // DB에서 Project Link 조회 가능한지 확인
                    val projectKey = UUID.fromString(projectKeyString)
                    val projectLink = formLinkJpaRepository.findByKey(projectKey)
                    projectLink.shouldNotBeNull()
                    projectLink.linkType shouldBe FormLinkType.PROJECT

                    // DB에서 Review Link 조회 가능한지 확인
                    val reviewKey = UUID.fromString(reviewKeyString)
                    val reviewLink = formLinkJpaRepository.findByKey(reviewKey)
                    reviewLink.shouldNotBeNull()
                    reviewLink.linkType shouldBe FormLinkType.MEMBER_REVIEW
                }
            }
        }

        context("teamCount가 0인 경우") {
            it("InvalidTeamCountException이 발생하고 DB에 아무것도 저장되지 않는다") {
                // given
                val generation = "14기"
                val teamCount = 0

                // when & then
                shouldThrow<InvalidTeamCountException> {
                    projectInitService.initProjects(generation, teamCount)
                }

                // DB에 아무것도 저장되지 않았는지 확인
                projectJpaRepository.findAll() shouldHaveSize 0
                formLinkJpaRepository.findAll() shouldHaveSize 0
            }
        }

        context("teamCount가 음수인 경우") {
            it("InvalidTeamCountException이 발생한다") {
                // given
                val generation = "14기"
                val teamCount = -5

                // when & then
                shouldThrow<InvalidTeamCountException> {
                    projectInitService.initProjects(generation, teamCount)
                }
            }
        }

        context("단일 팀만 생성하는 경우") {
            it("하나의 프로젝트와 FormLink만 생성된다") {
                // given
                val generation = "16기"
                val teamCount = 1

                // when
                val result = projectInitService.initProjects(generation, teamCount)

                // then
                result shouldHaveSize 1
                result[0].first shouldBe "1조"

                projectJpaRepository.findAll() shouldHaveSize 1
                formLinkJpaRepository.findAll() shouldHaveSize 2 // 1 project * 2 links
            }
        }

        context("많은 팀을 생성하는 경우") {
            it("모든 팀이 올바르게 생성된다") {
                // given
                val generation = "14기"
                val teamCount = 15

                // when
                val result = projectInitService.initProjects(generation, teamCount)

                // then
                result shouldHaveSize 15
                result.forEachIndexed { index, (teamName, _, _) ->
                    teamName shouldBe "${index + 1}조"
                }

                val savedProjects = projectJpaRepository.findAll()
                savedProjects shouldHaveSize 15

                val savedFormLinks = formLinkJpaRepository.findAll()
                savedFormLinks shouldHaveSize 30 // 15 teams * 2 links
            }
        }

        context("프로젝트와 FormLink의 연결 확인") {
            it("FormLink의 targetId가 해당 프로젝트의 ID와 일치한다") {
                // given
                val generation = "14기"
                val teamCount = 2

                // when
                val result = projectInitService.initProjects(generation, teamCount)

                // then
                val savedProjects = projectJpaRepository.findAll()
                val savedFormLinks = formLinkJpaRepository.findAll()

                savedFormLinks.forEach { formLink ->
                    val linkedProject = savedProjects.find { it.id == formLink.targetId }
                    linkedProject.shouldNotBeNull()
                }

                // 각 결과의 키로 FormLink를 찾고, targetId로 프로젝트를 찾아 팀명 검증
                result.forEachIndexed { index, (teamName, projectKeyString, reviewKeyString) ->
                    val projectKey = UUID.fromString(projectKeyString)
                    val projectLink = formLinkJpaRepository.findByKey(projectKey)
                    projectLink.shouldNotBeNull()

                    val project = projectJpaRepository.findById(projectLink.targetId).get()
                    project.generationInfo.teamName shouldBe teamName
                    project.generationInfo.teamName shouldBe "${index + 1}조"

                    val reviewKey = UUID.fromString(reviewKeyString)
                    val reviewLink = formLinkJpaRepository.findByKey(reviewKey)
                    reviewLink.shouldNotBeNull()

                    val reviewProject = projectJpaRepository.findById(reviewLink.targetId).get()
                    reviewProject.generationInfo.teamName shouldBe teamName
                }
            }
        }

        context("다른 기수명으로 프로젝트를 생성하는 경우") {
            it("기수 정보가 올바르게 저장된다") {
                // given
                val generation = "DND 14기 프로젝트"
                val teamCount = 2

                // when
                projectInitService.initProjects(generation, teamCount)

                // then
                val savedProjects = projectJpaRepository.findAll()
                savedProjects.forEach { project ->
                    project.generationInfo.generation shouldBe "DND 14기 프로젝트"
                }
                savedProjects[0].name shouldBe "DND 14기 프로젝트 1조"
                savedProjects[1].name shouldBe "DND 14기 프로젝트 2조"
            }
        }
    }
})