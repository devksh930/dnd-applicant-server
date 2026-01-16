package ac.dnd.server.review.infrastructure.web

import ac.dnd.server.annotation.RestDocsTest
import ac.dnd.server.documenation.DocumentUtils
import ac.dnd.server.documenation.MockMvcFactory
import ac.dnd.server.review.application.service.MemberReviewProjectResult
import ac.dnd.server.review.application.service.MemberReviewQueryService
import ac.dnd.server.review.infrastructure.web.dto.response.MemberReviewProjectResponse
import ac.dnd.server.review.infrastructure.web.dto.response.MemberReviewResponse
import ac.dnd.server.review.infrastructure.web.mapper.MemberReviewWebMapper
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.given
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RestDocsTest
class MemberReviewQueryControllerDocsTest {

    @Mock
    private lateinit var memberReviewQueryService: MemberReviewQueryService

    @Spy
    private var memberReviewWebMapper: MemberReviewWebMapper = MemberReviewWebMapper()

    @InjectMocks
    private lateinit var controller: MemberReviewQueryController

    companion object {
        private const val HOST_API = "api.dnd.ac"
        private const val LINK_KEY = "00000000-0000-0000-0000-000000000000"
    }

    @Test
    fun `멤버 회고 목록 조회 - 성공 200 문서화`(
        contextProvider: RestDocumentationContextProvider
    ) {
        // given
        val result = MemberReviewProjectResult(
            generation = "10",
            teamName = "1조",
            members = emptyList()
        )
        val response = MemberReviewProjectResponse(
            generation = "10",
            teamName = "1조",
            members = listOf(
                MemberReviewResponse(1L, "홍길동", "BACKEND", "PENDING"),
                MemberReviewResponse(2L, "임꺽정", "FRONTEND", "NONE")
            )
        )
        given(memberReviewQueryService.getMemberReviewsByLink(LINK_KEY)).willReturn(result)
        doReturn(response).whenever(memberReviewWebMapper).toResponse(any(), any(), any())

        // when & then
        MockMvcFactory.getRestDocsMockMvc(contextProvider, HOST_API, controller)
            .perform(
                RestDocumentationRequestBuilders.get("/member-review/{linkKey}", LINK_KEY)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk)
            .andDo(
                DocumentUtils.document(
                    "get-member-review-list-success",
                    "멤버 리뷰",
                    "멤버 리뷰 목록 조회",
                    "링크 키로 해당 프로젝트의 멤버 리뷰 목록을 조회합니다.",
                    pathParameters(
                        parameterWithName("linkKey").description("프로젝트 링크 키(UUID)")
                    ),
                    responseFields(
                        fieldWithPath("generation").type(JsonFieldType.STRING).description("기수"),
                        fieldWithPath("teamName").type(JsonFieldType.STRING).description("팀 이름"),
                        fieldWithPath("members").type(JsonFieldType.ARRAY).description("멤버 목록"),
                        fieldWithPath("members[].id").type(JsonFieldType.NUMBER).description("리뷰 ID"),
                        fieldWithPath("members[].name").type(JsonFieldType.STRING).description("이름"),
                        fieldWithPath("members[].position").type(JsonFieldType.STRING).description("포지션"),
                        fieldWithPath("members[].status").type(JsonFieldType.STRING).description("제출 상태")
                    )
                )
            )
    }
}
