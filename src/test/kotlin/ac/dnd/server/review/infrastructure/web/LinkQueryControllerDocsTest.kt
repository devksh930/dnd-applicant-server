package ac.dnd.server.review.infrastructure.web

import ac.dnd.server.annotation.RestDocsTest
import ac.dnd.server.documenation.DocumentFormatGenerator.generateEnumAttrs
import ac.dnd.server.documenation.DocumentFormatGenerator.generatedEnums
import ac.dnd.server.documenation.DocumentUtils.getDocumentRequest
import ac.dnd.server.documenation.DocumentUtils.getDocumentResponse
import ac.dnd.server.documenation.MockMvcFactory
import ac.dnd.server.review.application.service.LinkQueryService
import ac.dnd.server.review.domain.enums.FormLinkType
import ac.dnd.server.review.exception.FormLinkExpiredException
import ac.dnd.server.review.exception.FormLinkNotFoundException
import ac.dnd.server.review.infrastructure.web.dto.response.LinkQueryResponse
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@RestDocsTest
class LinkQueryControllerDocsTest {

    @Mock
    private lateinit var linkQueryService: LinkQueryService

    @InjectMocks
    private lateinit var controller: LinkQueryController

    companion object {
        private const val HOST_API = "api.dnd.ac"
        private const val LINK_KEY = "00000000-0000-0000-0000-000000000000"
    }

    private val successResponseFields = arrayOf(
        fieldWithPath("type").type(JsonFieldType.STRING)
            .attributes(generateEnumAttrs(FormLinkType::class.java, FormLinkType::description))
            .description(
                """
                링크 타입 (Enum)
                ${generatedEnums(FormLinkType::class.java)}
                """.trimIndent()
            ),
        fieldWithPath("expiredAt").type(JsonFieldType.STRING).description("링크 만료 시각")
    )

    private val errorResponseFields = arrayOf(
        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지"),
        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
        fieldWithPath("errors").type(JsonFieldType.ARRAY).description("필드 에러 목록(없을 수 있음)"),
        fieldWithPath("errors[].field").optional().type(JsonFieldType.STRING).description("에러가 발생한 필드명"),
        fieldWithPath("errors[].value").optional().type(JsonFieldType.STRING).description("거부된 값"),
        fieldWithPath("errors[].reason").optional().type(JsonFieldType.STRING).description("사유"),
        fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드")
    )

    @Test
    fun `링크 조회 - 성공 200 문서화`(
        contextProvider: RestDocumentationContextProvider
    ) {
        // given
        given(linkQueryService.getLinkInfo(any())).willReturn(
            LinkQueryResponse(
                type = FormLinkType.PROJECT,
                expiredAt = LocalDateTime.now().plusDays(1)
            )
        )

        // when & then
        MockMvcFactory.getRestDocsMockMvc(contextProvider, HOST_API, controller)
            .perform(
                RestDocumentationRequestBuilders.get("/link/{linkKey}", LINK_KEY)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk)
            .andDo(
                document(
                    "get-link-success",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pathParameters(
                        parameterWithName("linkKey").description("프로젝트 링크 키(UUID)")
                    ),
                    responseFields(*successResponseFields)
                )
            )
    }

    @Test
    fun `링크 조회 - 만료 422 문서화`(
        contextProvider: RestDocumentationContextProvider
    ) {
        // given
        given(linkQueryService.getLinkInfo(any())).willAnswer { throw FormLinkExpiredException() }

        // when & then
        MockMvcFactory.getRestDocsMockMvc(contextProvider, HOST_API, controller)
            .perform(
                RestDocumentationRequestBuilders.get("/link/{linkKey}", LINK_KEY)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isUnprocessableEntity)
            .andDo(
                document(
                    "get-link-expired",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pathParameters(
                        parameterWithName("linkKey").description("프로젝트 링크 키(UUID)")
                    ),
                    responseFields(*errorResponseFields)
                )
            )
    }

    @Test
    fun `링크 조회 - 미존재 404 문서화`(
        contextProvider: RestDocumentationContextProvider
    ) {
        // given
        given(linkQueryService.getLinkInfo(any())).willAnswer { throw FormLinkNotFoundException() }

        // when & then
        MockMvcFactory.getRestDocsMockMvc(contextProvider, HOST_API, controller)
            .perform(
                RestDocumentationRequestBuilders.get("/link/{linkKey}", LINK_KEY)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isNotFound)
            .andDo(
                document(
                    "get-link-not-found",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pathParameters(
                        parameterWithName("linkKey").description("프로젝트 링크 키(UUID)")
                    ),
                    responseFields(*errorResponseFields)
                )
            )
    }
}
