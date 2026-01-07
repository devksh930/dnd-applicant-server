package ac.dnd.server.account.infrastructure.web

import ac.dnd.server.account.infrastructure.persistence.repository.RefreshTokenRepository
import ac.dnd.server.account.infrastructure.security.resolver.LoginArgumentResolver
import ac.dnd.server.documenation.DocumentUtils
import ac.dnd.server.shared.dto.LoginInfo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName
import org.springframework.restdocs.cookies.CookieDocumentation.responseCookies
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.*

@ExtendWith(MockitoExtension::class, RestDocumentationExtension::class)
class AuthDocsTest {

    @Mock
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @InjectMocks
    private lateinit var authController: AuthController

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        val loginArgumentResolver = mock<LoginArgumentResolver>()
        whenever(loginArgumentResolver.supportsParameter(any())).thenReturn(true)
        whenever(loginArgumentResolver.resolveArgument(any(), any(), any(), any())).thenReturn(
            LoginInfo(
                userId = UUID.randomUUID()
            )
        )

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
            .setCustomArgumentResolvers(loginArgumentResolver)
            .apply<org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder>(
                documentationConfiguration(restDocumentation)
            )
            .build()
    }

    @Test
    fun `로그아웃 - 성공`() {
        mockMvc.perform(
            post("/auth/logout")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
        )
            .andDo(
                DocumentUtils.document(
                    "post-auth-logout",
                    "인증",
                    "로그아웃",
                    "사용자 로그아웃을 처리하고 리프레시 토큰을 만료시킵니다.",
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                    ),
                    responseCookies(
                        cookieWithName("refreshToken").description("삭제된 리프레시 토큰 (만료 처리됨)")
                    )
                )
            )
    }
}
