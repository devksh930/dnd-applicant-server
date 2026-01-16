package ac.dnd.server.review.infrastructure.adapter

import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.review.domain.enums.FormLinkType
import ac.dnd.server.review.domain.model.FormLink
import ac.dnd.server.review.domain.model.Project
import ac.dnd.server.review.exception.FormLinkExpiredException
import ac.dnd.server.review.infrastructure.persistence.entity.FormLinkEntity
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectEntity
import ac.dnd.server.review.infrastructure.persistence.mapper.ProjectPersistenceMapper
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

    val projectPersistenceMapper = ProjectPersistenceMapper()
    val adapter = ProjectRepositoryAdapter(
        projectJpaRepository,
        projectUrlJpaRepository,
        formLinkJpaRepository,
        projectPersistenceMapper
    )

    describe("findProjectByLinkKey") {

        it("만료된 FormLink면 FormLinkExpiredException을 던진다") {
            // given
            val key = UUID.randomUUID()
            val link = FormLinkEntity(
                linkType = FormLinkType.PROJECT,
                key = key,
                targetId = 1L,
                expired = true,
                expirationDateTime = LocalDateTime.now().minusDays(1)
            )
            val idField = FormLinkEntity::class.java.superclass.getDeclaredField("id")
            idField.isAccessible = true
            idField.set(link, 1L)
            whenever(formLinkJpaRepository.findByKey(key)).thenReturn(link)

            // expect
            shouldThrow<FormLinkExpiredException> {
                adapter.findProjectByLinkKey(key.toString())
            }
        }

        it("유효한 FormLink와 Project가 존재하면 Project를 반환한다") {
            // given
            val key = UUID.randomUUID()
            val link = FormLinkEntity(
                linkType = FormLinkType.PROJECT,
                key = key,
                targetId = 100L,
                expired = false,
                expirationDateTime = LocalDateTime.now().plusDays(1)
            )
            val linkIdField = FormLinkEntity::class.java.superclass.getDeclaredField("id")
            linkIdField.isAccessible = true
            linkIdField.set(link, 1L)

            val projectEntity = ProjectEntity(
                generationInfo = GenerationInfo("14기", "1조"),
                name = "14기 1조",
                description = ""
            )
            val projectIdField = ProjectEntity::class.java.superclass.getDeclaredField("id")
            projectIdField.isAccessible = true
            projectIdField.set(projectEntity, 100L)

            whenever(formLinkJpaRepository.findByKey(key)).thenReturn(link)
            whenever(projectJpaRepository.findById(100L)).thenReturn(Optional.of(projectEntity))

            // when
            val result = adapter.findProjectByLinkKey(key.toString())

            // then
            result!!.id shouldBe 100L
            result.name shouldBe "14기 1조"
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
