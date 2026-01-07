package ac.dnd.server.shared.web

import ac.dnd.server.annotation.RestDocsTest
import ac.dnd.server.common.exception.UnauthenticatedException
import ac.dnd.server.documenation.DocumentUtils
import ac.dnd.server.documenation.MockMvcFactory
import ac.dnd.server.review.exception.ProjectNotFoundException
import ac.dnd.server.review.exception.FormLinkExpiredException
import ac.dnd.server.review.exception.FormLinkNotFoundException
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.bind.annotation.*

@RestDocsTest
class GlobalExceptionHandlerDocsTest {

    private val controller = TestErrorController()

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
    fun `유효성 검증 실패 422 문서화`(contextProvider: RestDocumentationContextProvider) {
        val body = """
            { "name": "", "description": "" }
        """.trimIndent()

        MockMvcFactory.getRestDocsMockMvc(contextProvider, "api.dnd.ac", controller)
            .perform(
                RestDocumentationRequestBuilders.post("/docs/errors/validation")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andDo(print())
            .andExpect(status().isUnprocessableEntity)
            .andDo(
                DocumentUtils.document(
                    "error-validation",
                    "에러",
                    "유효성 검증 실패",
                    "요청 데이터의 유효성 검증에 실패했을 때 반환되는 오류입니다.",
                    responseFields(*errorResponseFields)
                )
            )
    }

    @Test
    fun `메서드 미지원 405 문서화`(contextProvider: RestDocumentationContextProvider) {
        MockMvcFactory.getRestDocsMockMvc(contextProvider, "api.dnd.ac", controller)
            .perform(
                RestDocumentationRequestBuilders.post("/docs/errors/method-not-allowed")
            )
            .andDo(print())
            .andExpect(status().isMethodNotAllowed)
            .andDo(
                DocumentUtils.document(
                    "error-method-not-allowed",
                    "에러",
                    "메서드 미지원",
                    "지원하지 않는 HTTP 메서드로 요청했을 때 반환되는 오류입니다.",
                    responseFields(*errorResponseFields)
                )
            )
    }

    @Test
    fun `타입 미스매치 400 문서화`(contextProvider: RestDocumentationContextProvider) {
        MockMvcFactory.getRestDocsMockMvc(contextProvider, "api.dnd.ac", controller)
            .perform(
                RestDocumentationRequestBuilders.get("/docs/errors/type-mismatch/{id}", "abc")
            )
            .andDo(print())
            .andExpect(status().isBadRequest)
            .andDo(
                DocumentUtils.document(
                    "error-type-mismatch",
                    "에러",
                    "타입 불일치",
                    "요청 파라미터의 타입이 일치하지 않을 때 반환되는 오류입니다.",
                    responseFields(*errorResponseFields)
                )
            )
    }

    @Test
    fun `비즈니스 예외 - 프로젝트 조회 실패 404 문서화`(contextProvider: RestDocumentationContextProvider) {
        MockMvcFactory.getRestDocsMockMvc(contextProvider, "api.dnd.ac", controller)
            .perform(RestDocumentationRequestBuilders.get("/docs/errors/business/not-found"))
            .andDo(print())
            .andExpect(status().isNotFound)
            .andDo(
                DocumentUtils.document(
                    "error-business-not-found",
                    "에러",
                    "프로젝트 조회 실패",
                    "존재하지 않는 프로젝트를 조회할 때 반환되는 오류입니다.",
                    responseFields(*errorResponseFields)
                )
            )
    }

    @Test
    fun `비즈니스 예외 - 링크 조회 실패 404 문서화`(contextProvider: RestDocumentationContextProvider) {
        MockMvcFactory.getRestDocsMockMvc(contextProvider, "api.dnd.ac", controller)
            .perform(RestDocumentationRequestBuilders.get("/docs/errors/business/link-not-found"))
            .andDo(print())
            .andExpect(status().isNotFound)
            .andDo(
                DocumentUtils.document(
                    "error-business-link-not-found",
                    "에러",
                    "링크 조회 실패",
                    "존재하지 않는 링크를 조회할 때 반환되는 오류입니다.",
                    responseFields(*errorResponseFields)
                )
            )
    }

    @Test
    fun `비즈니스 예외 - 링크 만료 422 문서화`(contextProvider: RestDocumentationContextProvider) {
        MockMvcFactory.getRestDocsMockMvc(contextProvider, "api.dnd.ac", controller)
            .perform(RestDocumentationRequestBuilders.get("/docs/errors/business/expired"))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity)
            .andDo(
                DocumentUtils.document(
                    "error-business-expired",
                    "에러",
                    "링크 만료",
                    "만료된 링크를 사용하려 할 때 반환되는 오류입니다.",
                    responseFields(*errorResponseFields)
                )
            )
    }

    @Test
    fun `인증 예외 403 문서화`(contextProvider: RestDocumentationContextProvider) {
        MockMvcFactory.getRestDocsMockMvc(contextProvider, "api.dnd.ac", controller)
            .perform(RestDocumentationRequestBuilders.get("/docs/errors/auth"))
            .andDo(print())
            .andExpect(status().isForbidden)
            .andDo(
                DocumentUtils.document(
                    "error-auth-unauthenticated",
                    "에러",
                    "인증 실패",
                    "인증되지 않은 사용자가 접근할 때 반환되는 오류입니다.",
                    responseFields(*errorResponseFields)
                )
            )
    }

    @Test
    fun `서버 내부 오류 500 문서화`(contextProvider: RestDocumentationContextProvider) {
        MockMvcFactory.getRestDocsMockMvc(contextProvider, "api.dnd.ac", controller)
            .perform(RestDocumentationRequestBuilders.get("/docs/errors/internal"))
            .andDo(print())
            .andExpect(status().isInternalServerError)
            .andDo(
                DocumentUtils.document(
                    "error-internal",
                    "에러",
                    "서버 내부 오류",
                    "서버에서 예기치 않은 오류가 발생했을 때 반환되는 오류입니다.",
                    responseFields(*errorResponseFields)
                )
            )
    }

    @RestController
    @RequestMapping("/docs/errors")
    class TestErrorController {

        data class CreateReq(
            @field:NotBlank(message = "이름은 필수입니다")
            val name: String?,
            @field:NotBlank(message = "설명은 필수입니다")
            val description: String?
        )

        @PostMapping("/validation")
        fun validation(@Valid @RequestBody req: CreateReq) { }

        @GetMapping("/method-not-allowed")
        fun methodAllowed(): String = "OK"

        @GetMapping("/type-mismatch/{id}")
        fun typeMismatch(@PathVariable id: Long): String = id.toString()

        @GetMapping("/business/not-found")
        fun businessNotFound(): String { throw ProjectNotFoundException() }

        @GetMapping("/business/expired")
        fun businessExpired(): String { throw FormLinkExpiredException() }

        @GetMapping("/business/link-not-found")
        fun businessLinkNotFound(): String { throw FormLinkNotFoundException() }

        @GetMapping("/auth")
        fun auth(): String { throw UnauthenticatedException() }

        @GetMapping("/internal")
        fun internal(): String { throw RuntimeException("boom") }
    }
}
