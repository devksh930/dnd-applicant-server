package ac.dnd.server.review.infrastructure.web

import ac.dnd.server.annotation.RestDocsTest
import ac.dnd.server.documenation.DocumentUtils
import ac.dnd.server.documenation.MockMvcFactory
import ac.dnd.server.review.application.service.ProjectInitService
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@RestDocsTest
class ProjectInitControllerDocsTest {

    @Mock
    private lateinit var projectInitService: ProjectInitService

    @InjectMocks
    private lateinit var controller: ProjectInitController

    companion object {
        private const val HOST_API = "api.dnd.ac"
    }

    @Test
    fun `프로젝트 초기화 API 문서화`(
        contextProvider: RestDocumentationContextProvider
    ) {
        // given
        val request = """
            {
                "generation": "14기",
                "teamCount": 2
            }
        """.trimIndent()

        val results = listOf(
            "1조" to "00000000-0000-0000-0000-000000000001",
            "2조" to "00000000-0000-0000-0000-000000000002"
        )
        given(projectInitService.initProjects(any(), any())).willReturn(results)

        // when & then
        MockMvcFactory.getRestDocsMockMvc(
            contextProvider,
            HOST_API,
            controller
        ).perform(
            RestDocumentationRequestBuilders.post("/project/init")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                DocumentUtils.document(
                    "post-project-init",
                    "프로젝트",
                    "프로젝트 초기화",
                    "새 기수의 프로젝트를 초기화하고 팀별 제출 링크를 생성합니다.",
                    requestFields(
                        fieldWithPath("generation").type(JsonFieldType.STRING).description("기수"),
                        fieldWithPath("teamCount").type(JsonFieldType.NUMBER).description("팀 개수")
                    ),
                    responseFields(
                        fieldWithPath("generation").type(JsonFieldType.STRING).description("기수"),
                        fieldWithPath("teamCount").type(JsonFieldType.NUMBER).description("생성된 팀 개수"),
                        fieldWithPath("links").type(JsonFieldType.ARRAY).description("생성된 링크 목록"),
                        fieldWithPath("links[].teamName").type(JsonFieldType.STRING).description("팀 이름"),
                        fieldWithPath("links[].linkKey").type(JsonFieldType.STRING).description("링크 키 (UUID)")
                    )
                )
            )
    }
}
