package ac.dnd.server.admission.infrastructure.web

import ac.dnd.server.admission.application.service.EventQueryService
import ac.dnd.server.admission.domain.enums.EventStatus
import ac.dnd.server.admission.domain.model.EventData
import ac.dnd.server.admission.domain.model.ViewablePeriod
import ac.dnd.server.annotation.RestDocsTest
import ac.dnd.server.documenation.DocumentUtils
import ac.dnd.server.documenation.MockMvcFactory
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.given
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

@RestDocsTest
class EventQueryControllerDocsTest {

    @Mock
    private lateinit var eventQueryService: EventQueryService

    @InjectMocks
    private lateinit var controller: EventQueryController

    companion object {
        private const val HOST_API = "api.dnd.ac"
    }

    @Test
    fun `현재 진행중인 이벤트 조회 API 문서화`(
        contextProvider: RestDocumentationContextProvider
    ) {
        // given
        val eventData = EventData(
            id = 1L,
            name = "14기 모집",
            period = ViewablePeriod(
                startDate = LocalDateTime.now().minusDays(1),
                endDate = LocalDateTime.now().plusDays(1)
            ),
            resultAnnouncementDateTime = LocalDateTime.now().plusHours(1),
            status = EventStatus.PENDING
        )

        given(eventQueryService.getCurrentEvent()).willReturn(eventData)

        // when & then
        MockMvcFactory.getRestDocsMockMvc(
            contextProvider,
            HOST_API,
            controller
        ).perform(
            RestDocumentationRequestBuilders.get("/events/current")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                DocumentUtils.document(
                    "get-events-current",
                    "이벤트",
                    "현재 이벤트 조회",
                    "현재 진행 중인 모집 이벤트 정보를 조회합니다.",
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("이벤트 ID"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("이벤트 이름"),
                        fieldWithPath("resultAnnouncementDateTime").type(JsonFieldType.STRING).description("합격자 발표 일시"),
                        fieldWithPath("isResultAnnounced").type(JsonFieldType.BOOLEAN).description("합격자 발표 여부")
                    )
                )
            )
    }
}
