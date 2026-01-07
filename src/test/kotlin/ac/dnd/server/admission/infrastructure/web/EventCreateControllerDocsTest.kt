package ac.dnd.server.admission.infrastructure.web

import ac.dnd.server.admission.application.service.EventCreateService
import ac.dnd.server.annotation.RestDocsTest
import ac.dnd.server.documenation.DocumentUtils
import ac.dnd.server.documenation.MockMvcFactory
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

@RestDocsTest
class EventCreateControllerDocsTest {

    @Mock
    private lateinit var eventCreateService: EventCreateService

    @InjectMocks
    private lateinit var controller: EventCreateController

    companion object {
        private const val HOST_API = "api.dnd.ac"
    }

    @Test
    fun `이벤트 생성 API 문서화`(
        contextProvider: RestDocumentationContextProvider
    ) {
        // given
        val request = """
            {
                "name": "14기 모집",
                "startDateTime": "${LocalDateTime.now()}",
                "endDateTime": "${LocalDateTime.now().plusDays(7)}"
            }
        """.trimIndent()

        given(eventCreateService.createEvent(any())).willReturn(1L)

        // when & then
        MockMvcFactory.getRestDocsMockMvc(
            contextProvider,
            HOST_API,
            controller
        ).perform(
            RestDocumentationRequestBuilders.post("/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(
                DocumentUtils.document(
                    "post-event-create",
                    "이벤트",
                    "이벤트 생성",
                    "새로운 모집 이벤트를 생성합니다.",
                    requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("이벤트 이름"),
                        fieldWithPath("startDateTime").type(JsonFieldType.STRING).description("합격 조회 시작 일시"),
                        fieldWithPath("endDateTime").type(JsonFieldType.STRING).description("합격 조회 종료 일시")
                    ),
                    responseHeaders(
                        headerWithName("Location").description("생성된 이벤트 리소스 위치")
                    )
                )
            )
    }
}
