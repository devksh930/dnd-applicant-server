package ac.dnd.server.review.infrastructure.adapter

import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.review.domain.enums.FormLinkType
import ac.dnd.server.review.exception.FormLinkExpiredException
import ac.dnd.server.review.infrastructure.persistence.entity.FormLink
import ac.dnd.server.review.infrastructure.persistence.entity.Project
import ac.dnd.server.review.infrastructure.persistence.repository.FormLinkJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.ProjectJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.ProjectUrlJpaRepository
import ac.dnd.server.review.domain.value.GenerationInfo
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

@UnitTest
class ProjectRepositoryAdapterTest : DescribeSpec({

    val projectJpaRepository: ProjectJpaRepository = mock()
    val projectUrlJpaRepository: ProjectUrlJpaRepository = mock()
    val formLinkJpaRepository: FormLinkJpaRepository = mock()

    val adapter = ProjectRepositoryAdapter(
        projectJpaRepository,
        projectUrlJpaRepository,
        formLinkJpaRepository
    )

    describe("findProjectByLinkKey") {

        it("만료된 FormLink면 FormLinkExpiredException을 던진다") {
            // given
            val key = UUID.randomUUID()
            val link = FormLink(
                linkType = FormLinkType.PROJECT,
                key = key,
                targetId = 1L,
                expired = true,
                expirationDateTime = LocalDateTime.now().minusDays(1)
            )
            whenever(formLinkJpaRepository.findByKey(key)).thenReturn(link)

            // expect
            shouldThrow<FormLinkExpiredException> {
                adapter.findProjectByLinkKey(key.toString())
            }
        }

        it("유효한 FormLink와 Project가 존재하면 Project를 반환한다") {
            // given
            val key = UUID.randomUUID()
            val link = FormLink(
                linkType = FormLinkType.PROJECT,
                key = key,
                targetId = 100L,
                expired = false,
                expirationDateTime = LocalDateTime.now().plusDays(1)
            )
            val project = Project(
                generationInfo = GenerationInfo("14기", "1조"),
                name = "14기 1조",
                description = ""
            )
            whenever(formLinkJpaRepository.findByKey(key)).thenReturn(link)
            whenever(projectJpaRepository.findById(100L)).thenReturn(Optional.of(project))

            // when
            val result = adapter.findProjectByLinkKey(key.toString())

            // then
            result shouldBe project
        }

        it("FormLink가 없으면 null을 반환한다") {
            // given
            val key = UUID.randomUUID()
            whenever(formLinkJpaRepository.findByKey(key)).thenReturn(null)

            // when
            val result = adapter.findProjectByLinkKey(key.toString())

            // then
            result.shouldBeNull()
        }
    }
})

inline fun <reified T : Throwable> shouldThrow(block: () -> Any?) {
    try {
        block.invoke()
        throw AssertionError("Expected exception ${T::class.java.simpleName} was not thrown")
    } catch (e: Throwable) {
        if (e is T) return
        throw AssertionError("Unexpected exception thrown: ${e::class.java.name}", e)
    }
}
