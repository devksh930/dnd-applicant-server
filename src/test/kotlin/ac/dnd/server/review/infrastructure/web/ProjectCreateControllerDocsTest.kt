package ac.dnd.server.review.infrastructure.web

import ac.dnd.server.annotation.RestDocsTest
import ac.dnd.server.documenation.DocumentFormatGenerator.generateEnumAttrs
import ac.dnd.server.documenation.DocumentFormatGenerator.generatedEnums
import ac.dnd.server.documenation.DocumentUtils
import ac.dnd.server.documenation.MockMvcFactory
import ac.dnd.server.review.application.dto.command.ProjectCreateCommand
import ac.dnd.server.review.application.service.ProjectCreateService
import ac.dnd.server.review.domain.enums.UrlType
import ac.dnd.server.review.exception.FormLinkExpiredException
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.willThrow
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RestDocsTest
class ProjectCreateControllerDocsTest {

    @Mock
    private lateinit var projectCreateService: ProjectCreateService

    @InjectMocks
    private lateinit var controller: ProjectCreateController

    companion object {
        private const val HOST_API = "api.dnd.ac"
        private const val LINK_KEY = "00000000-0000-0000-0000-000000000000"
    }

    private fun requestJson(): String =
        """
        {
          "name": "DND 14기 1조",
          "description": "DND 14기 1조 프로젝트 소개",
          "techStacks": ["Kotlin", "Spring", "JPA"],
          "fileId": 123,
          "urlLinks": [
            {"type": "GITHUB", "url": "https://github.com/dnd-14/team-1", "order": 0},
            {"type": "YOUTUBE", "url": "https://youtu.be/demo", "order": 1}
          ]
        }
        """.trimIndent()

    private val requestFieldDescription = arrayOf(
        fieldWithPath("name").type(JsonFieldType.STRING).description("프로젝트 이름"),
        fieldWithPath("description").type(JsonFieldType.STRING).description("프로젝트 설명"),
        fieldWithPath("techStacks").optional().type(JsonFieldType.ARRAY).description("기술 스택 목록(선택)"),
        fieldWithPath("fileId").optional().type(JsonFieldType.NUMBER).description("첨부 파일 ID(선택)"),
        fieldWithPath("urlLinks").optional().type(JsonFieldType.ARRAY).description("외부 링크 목록(선택)"),
        fieldWithPath("urlLinks[].type").optional().type(JsonFieldType.STRING)
            .attributes(generateEnumAttrs(UrlType::class.java, UrlType::description))
            .description(
                """
                링크 타입 (Enum)
                ${generatedEnums(UrlType::class.java)}
                """.trimIndent()
            ),
        fieldWithPath("urlLinks[].url").optional().type(JsonFieldType.STRING).description("링크 URL"),
        fieldWithPath("urlLinks[].order").optional().type(JsonFieldType.NUMBER).description("표시 순서(선택)")
    )

    @Test
    fun `프로젝트 제출 - 최초 제출 201 문서화`(
        contextProvider: RestDocumentationContextProvider
    ) {
        // given
        given(projectCreateService.createProject(any<ProjectCreateCommand>()))
            .willReturn(true)

        // when & then
        MockMvcFactory.getRestDocsMockMvc(
            contextProvider,
            HOST_API,
            controller
        ).perform(
            RestDocumentationRequestBuilders.put("/project/{linkKey}", LINK_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson())
        )
            .andDo(print())
            .andExpect(status().isCreated)
            .andExpect(header().exists("Location"))
            .andDo(
                DocumentUtils.document(
                    "put-project-first",
                    "프로젝트",
                    "프로젝트 최초 제출",
                    "프로젝트를 최초로 제출합니다. 리소스가 생성되어 201 응답을 반환합니다.",
                    pathParameters(
                        parameterWithName("linkKey").description("프로젝트 링크 키(UUID)")
                    ),
                    requestFields(*requestFieldDescription),
                    responseHeaders(
                        headerWithName("Location").description("리소스 위치")
                    )
                )
            )
    }

    @Test
    fun `프로젝트 제출 - 재제출 204 문서화`(
        contextProvider: RestDocumentationContextProvider
    ) {
        // given
        given(projectCreateService.createProject(any<ProjectCreateCommand>()))
            .willReturn(false)

        // when & then
        MockMvcFactory.getRestDocsMockMvc(
            contextProvider,
            HOST_API,
            controller
        ).perform(
            RestDocumentationRequestBuilders.put("/project/{linkKey}", LINK_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson())
        )
            .andDo(print())
            .andExpect(status().isNoContent)
            .andDo(
                DocumentUtils.document(
                    "put-project-resubmission",
                    "프로젝트",
                    "프로젝트 재제출",
                    "기존 프로젝트를 수정하여 재제출합니다. 204 응답을 반환합니다.",
                    pathParameters(
                        parameterWithName("linkKey").description("프로젝트 링크 키(UUID)")
                    ),
                    requestFields(*requestFieldDescription)
                )
            )
    }

    @Test
    fun `프로젝트 제출 - 만료 링크 422 문서화`(
        contextProvider: RestDocumentationContextProvider
    ) {
        // given
        given(projectCreateService.createProject(any<ProjectCreateCommand>()))
            .willThrow(FormLinkExpiredException())

        // when & then
        MockMvcFactory.getRestDocsMockMvc(
            contextProvider,
            HOST_API,
            controller
        ).perform(
            RestDocumentationRequestBuilders.put("/project/{linkKey}", LINK_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson())
        )
            .andDo(print())
            .andExpect(status().isUnprocessableEntity)
            .andDo(
                DocumentUtils.document(
                    "put-project-expired",
                    "프로젝트",
                    "프로젝트 제출 실패 (링크 만료)",
                    "만료된 링크로 프로젝트를 제출하려 할 때 반환되는 오류입니다.",
                    pathParameters(
                        parameterWithName("linkKey").description("프로젝트 링크 키(UUID)")
                    ),
                    requestFields(*requestFieldDescription),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                        fieldWithPath("errors").type(JsonFieldType.ARRAY).description("에러 상세 목록")
                    )
                )
            )
    }

}
