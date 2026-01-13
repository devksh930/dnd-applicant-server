package ac.dnd.server.review.infrastructure.web

import ac.dnd.server.review.application.dto.command.ProjectCreateCommand
import ac.dnd.server.review.application.service.ProjectCreateService
import ac.dnd.server.review.exception.FormLinkExpiredException
import ac.dnd.server.review.infrastructure.web.dto.request.ProjectCreateRequest
import ac.dnd.server.documenation.MockMvcFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

@ExtendWith(MockitoExtension::class)
class ProjectCreateControllerTest {

    @Mock
    lateinit var projectCreateService: ProjectCreateService

    @InjectMocks
    lateinit var controller: ProjectCreateController

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `최초 제출이면 201 Created와 Location 헤더를 반환한다`() {
        // given
        whenever(projectCreateService.createProject(any<ProjectCreateCommand>())).thenReturn(true)

        val request = ProjectCreateRequest(
            name = "프로젝트 A",
            description = "설명 A",
            techStacks = listOf("Kotlin", "Spring")
        )

        val mockMvc = MockMvcFactory.getMockMvc(controller)

        // when & then
        mockMvc.perform(
            put("/project/{linkKey}", "00000000-0000-0000-0000-000000000000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "/project/00000000-0000-0000-0000-000000000000"))
    }

    @Test
    fun `재제출이면 204 No Content를 반환한다`() {
        // given
        whenever(projectCreateService.createProject(any<ProjectCreateCommand>())).thenReturn(false)

        val request = ProjectCreateRequest(
            name = "프로젝트 B",
            description = "설명 B"
        )

        val mockMvc = MockMvcFactory.getMockMvc(controller)

        // when & then
        mockMvc.perform(
            put("/project/{linkKey}", "11111111-1111-1111-1111-111111111111")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNoContent)
    }

    @Test
    fun `만료된 링크로 제출하면 422와 PJ002 에러 코드를 반환한다`() {
        // given
        whenever(projectCreateService.createProject(any<ProjectCreateCommand>())).thenThrow(FormLinkExpiredException())

        val request = ProjectCreateRequest(
            name = "프로젝트 C",
            description = "설명 C"
        )

        val mockMvc = MockMvcFactory.getMockMvc(controller)

        // when & then
        mockMvc.perform(
            put("/project/{linkKey}", "22222222-2222-2222-2222-222222222222")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.code").value("PJ002"))
            .andExpect(jsonPath("$.status").value(422))
            .andExpect(jsonPath("$.message").exists())
    }
}
