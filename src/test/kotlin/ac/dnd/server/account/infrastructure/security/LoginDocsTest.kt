package ac.dnd.server.account.infrastructure.security

import ac.dnd.server.account.infrastructure.security.authentication.filter.JsonUsernamePasswordAuthenticationFilter
import ac.dnd.server.account.infrastructure.security.dto.UserLoginRequest
import ac.dnd.server.common.util.JsonUtils
import ac.dnd.server.documenation.DocumentUtils.getDocumentRequest
import ac.dnd.server.documenation.DocumentUtils.getDocumentResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(RestDocumentationExtension::class, MockitoExtension::class)
class LoginDocsTest {

    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var authenticationManager: AuthenticationManager

    private val objectMapper = JsonUtils.getMapper()

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        val successHandler = AuthenticationSuccessHandler { _, response, _ ->
            response.status = 200
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.characterEncoding = "UTF-8"
            response.writer.write(
                objectMapper.writeValueAsString(
                    mapOf(
                        "accessToken" to "mock-access-token",
                        "expiresIn" to 3600
                    )
                )
            )
        }

        val filter = JsonUsernamePasswordAuthenticationFilter(objectMapper)
        filter.setAuthenticationManager(authenticationManager)
        filter.setAuthenticationSuccessHandler(successHandler)

        val builder = MockMvcBuilders.standaloneSetup(object {})
        builder.apply<StandaloneMockMvcBuilder>(documentationConfiguration(restDocumentation))
        builder.addFilter<StandaloneMockMvcBuilder>(filter)
        mockMvc = builder.build()
    }

    @Test
    fun `로그인 - 성공`() {
        val loginRequest = UserLoginRequest("test@example.com", "password1234")

        // given
        given(authenticationManager.authenticate(any()))
            .willReturn(UsernamePasswordAuthenticationToken("user", "password", emptyList()))

        // when & then
        mockMvc.perform(
            post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "post-auth-login",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                        fieldWithPath("expiresIn").type(JsonFieldType.NUMBER).description("만료 시간(초)")
                    )
                )
            )
    }
}
