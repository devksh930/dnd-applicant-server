package ac.dnd.server.review.infrastructure.adapter

import ac.dnd.server.review.domain.enums.FormLinkType
import ac.dnd.server.review.domain.model.Project
import ac.dnd.server.review.domain.value.GenerationInfo
import ac.dnd.server.review.exception.FormLinkExpiredException
import ac.dnd.server.review.infrastructure.persistence.entity.FormLinkEntity
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectEntity
import ac.dnd.server.review.infrastructure.persistence.mapper.ProjectPersistenceMapper
import ac.dnd.server.review.infrastructure.persistence.repository.FormLinkJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.ProjectJpaRepository
import ac.dnd.server.review.infrastructure.persistence.repository.ProjectUrlJpaRepository
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class ProjectRepositoryAdapterJUnitTest {

    @Mock
    lateinit var projectJpaRepository: ProjectJpaRepository

    @Mock
    lateinit var projectUrlJpaRepository: ProjectUrlJpaRepository

    @Mock
    lateinit var formLinkJpaRepository: FormLinkJpaRepository

    @Mock
    lateinit var projectPersistenceMapper: ProjectPersistenceMapper

    @InjectMocks
    lateinit var adapter: ProjectRepositoryAdapter

    @Test
    fun `만료된 FormLink면 예외를 던진다`() {
        // given
        val key = UUID.randomUUID()
        val link = FormLinkEntity(
            linkType = FormLinkType.PROJECT,
            key = key,
            targetId = 1L,
            expired = true,
            expirationDateTime = LocalDateTime.now().minusDays(1)
        )
        whenever(formLinkJpaRepository.findByKey(key)).thenReturn(link)

        // expect
        assertThrows(FormLinkExpiredException::class.java) {
            adapter.findProjectByLinkKey(key.toString())
        }
    }

    @Test
    fun `유효한 FormLink와 Project가 존재하면 Project를 반환한다`() {
        // given
        val key = UUID.randomUUID()
        val link = FormLinkEntity(
            linkType = FormLinkType.PROJECT,
            key = key,
            targetId = 100L,
            expired = false,
            expirationDateTime = LocalDateTime.now().plusDays(1)
        )
        val projectEntity = ProjectEntity(
            generationInfo = GenerationInfo("14기", "1조"),
            name = "14기 1조",
            description = ""
        )
        val projectDomain = Project(
            id = 100L,
            generationInfo = GenerationInfo("14기", "1조"),
            name = "14기 1조",
            description = ""
        )
        whenever(formLinkJpaRepository.findByKey(key)).thenReturn(link)
        whenever(projectJpaRepository.findById(100L)).thenReturn(Optional.of(projectEntity))
        whenever(projectPersistenceMapper.toDomain(projectEntity)).thenReturn(projectDomain)

        // when
        val result = adapter.findProjectByLinkKey(key.toString())

        // then
        assertSame(projectDomain, result)
    }

    @Test
    fun `FormLink가 없으면 null을 반환한다`() {
        // given
        val key = UUID.randomUUID()
        whenever(formLinkJpaRepository.findByKey(key)).thenReturn(null)

        // when
        val result = adapter.findProjectByLinkKey(key.toString())

        // then
        assertNull(result)
    }
}
