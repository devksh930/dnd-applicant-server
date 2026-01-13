package ac.dnd.server.notification

import ac.dnd.server.annotation.RestDocsTest
import ac.dnd.server.documenation.DocumentUtils
import ac.dnd.server.documenation.MockMvcFactory
import ac.dnd.server.notification.github.dto.GitHubIssueResponse
import ac.dnd.server.notification.github.service.GitHubService
import com.fasterxml.jackson.databind.ObjectMapper
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@RestDocsTest
class GoogleFormControllerDocsTest {

    @Mock
    private lateinit var gitHubService: GitHubService

    @InjectMocks
    private lateinit var controller: GoogleFromController

    companion object {
        private const val HOST_API = "api.dnd.ac"
    }

    @Test
    fun `구글 폼 연동 API 문서화`(
        contextProvider: RestDocumentationContextProvider
    ) {
        // given
        val request = mapOf(
            "이름은?" to "홍길동",
            "연락처는?" to "010-1234-5678"
        )
        val objectMapper = ObjectMapper()

        given(gitHubService.createIssue(any(), any())).willReturn(
            GitHubIssueResponse(
                id = 1L,
                number = 1,
                title = "이슈 제목",
                body = "이슈 내용",
                htmlUrl = "https://github.com/test/issues/1",
                state = "open",
                createdAt = "2024-01-01T00:00:00Z"
            )
        )

        // when & then
        MockMvcFactory.getRestDocsMockMvc(
            contextProvider,
            HOST_API,
            controller
        ).perform(
            RestDocumentationRequestBuilders.post("/google/form")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                DocumentUtils.document(
                    "post-google-form",
                    "알림",
                    "구글 폼 연동",
                    "구글 폼 제출 데이터를 받아 GitHub 이슈로 생성합니다.",
                    requestFields(
                        fieldWithPath("*").type(JsonFieldType.STRING).description("질문 내용과 답변 (동적 필드)")
                    )
                )
            )
    }
}
