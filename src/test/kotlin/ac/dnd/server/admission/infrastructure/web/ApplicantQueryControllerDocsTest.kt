package ac.dnd.server.admission.infrastructure.web

import ac.dnd.server.admission.application.dto.ApplicantStatusCheckCommand
import ac.dnd.server.admission.application.service.ApplicantQueryService
import ac.dnd.server.admission.infrastructure.web.mapper.ApplicantWebMapper
import ac.dnd.server.annotation.RestDocsTest
import ac.dnd.server.documenation.DocumentFormatGenerator.generateEnumAttrs
import ac.dnd.server.documenation.DocumentFormatGenerator.generatedEnums
import ac.dnd.server.documenation.DocumentUtils.getDocumentRequest
import ac.dnd.server.documenation.DocumentUtils.getDocumentResponse
import ac.dnd.server.documenation.MockMvcFactory
import ac.dnd.server.enums.ApplicantStatus
import ac.dnd.server.fixture.ApplicantDataFixture
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@RestDocsTest
class ApplicantQueryControllerDocsTest {

    @Mock
    private lateinit var applicantQueryService: ApplicantQueryService

    @Mock
    private lateinit var applicantWebMapper: ApplicantWebMapper

    @InjectMocks
    private lateinit var controller: ApplicantQueryController

    companion object {
        private const val HOST_API = "api.dnd.ac" // 예시 호스트
    }


    @Test
    fun `지원자 상태 확인 API 문서화`(
        contextProvider: RestDocumentationContextProvider
    ) {
        // given
        val createRequest = """
            {
                "name":"홍길동",
                "email":"test@test.com"
            }
        """.trimIndent()

        val requestFieldDescription = arrayOf(
            fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
            fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
        )

        given(
            applicantWebMapper.statusCheckRequestToCommand(
                any(),
                eq(1L)
            )
        ).willReturn(
            ApplicantStatusCheckCommand(
                1L,
                "홍길동",
                "test@test.com"
            )
        )

        val fixture = ApplicantDataFixture.create()
        given(applicantQueryService.getApplicantStatusCheck(any()))
            .willReturn(fixture)

        val responseFieldDescription = arrayOf(
            fieldWithPath("eventName").type(JsonFieldType.STRING).description("이벤트명"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("지원자명"),
            fieldWithPath("status").type(JsonFieldType.STRING)
                .attributes(
                    generateEnumAttrs(
                        ApplicantStatus::class.java,
                        ApplicantStatus::description
                    )
                ).description(
                    """
                    지원 상태 
                    ${generatedEnums(ApplicantStatus::class.java)}
                    """.trimIndent()
                )
        )

        // when & then
        MockMvcFactory.getRestDocsMockMvc(
            contextProvider,
            HOST_API,
            controller
        ).perform(
            RestDocumentationRequestBuilders.post(
                "/event/{eventId}/applicant/status/check", 1
            )
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequest)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo( // restDocs
                MockMvcRestDocumentation.document(
                    "get-applicant-status",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pathParameters(
                        parameterWithName("eventId").description("이벤트 ID")
                    ),
                    requestFields(*requestFieldDescription),
                    responseFields(*responseFieldDescription)
                )
            )
    }
}
