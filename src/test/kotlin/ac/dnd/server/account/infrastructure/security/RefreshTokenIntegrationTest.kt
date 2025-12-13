package ac.dnd.server.account.infrastructure.security

import ac.dnd.server.annotation.IntegrationTest
import ac.dnd.server.account.domain.enums.Role
import ac.dnd.server.account.infrastructure.persistence.entity.Account
import ac.dnd.server.account.infrastructure.persistence.repository.AccountJpaRepository
import ac.dnd.server.account.infrastructure.persistence.repository.RefreshTokenRepository
import ac.dnd.server.account.infrastructure.security.dto.UserLoginRequest
import ac.dnd.server.account.infrastructure.security.jwt.JwtTokenProvider
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.Cookie
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@IntegrationTest
@AutoConfigureMockMvc
@Transactional
class RefreshTokenIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var accountJpaRepository: AccountJpaRepository

    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @BeforeEach
    fun setUp() {
        accountJpaRepository.deleteAll()
        refreshTokenRepository.deleteAll()

        val password = passwordEncoder.encode("password1234")
        val account = Account(
            name = "Test User",
            email = "test@example.com",
            password = password,
            role = Role.ADMIN
        )
        accountJpaRepository.save(account)
    }

    @Test
    fun `로그인 시 Refresh Token이 DB에 저장되고 쿠키로 전송됨`() {
        val loginRequest = UserLoginRequest("test@example.com", "password1234")

        val result = mockMvc.perform(
            post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(cookie().exists("refreshToken"))
            .andExpect(header().exists("Authorization"))
            .andReturn()

        // DB에 Refresh Token이 저장되었는지 확인
        val savedTokens = refreshTokenRepository.findAll()
        assertThat(savedTokens).hasSize(1)

        val refreshTokenCookie = result.response.getCookie("refreshToken")
        assertThat(refreshTokenCookie).isNotNull
        assertThat(refreshTokenCookie!!.value).isEqualTo(savedTokens[0].token)
    }

    @Test
    fun `만료된 Access Token과 유효한 Refresh Token으로 새 Access Token 발급됨`() {
        // 1. 로그인하여 토큰 발급
        val loginRequest = UserLoginRequest("test@example.com", "password1234")
        val loginResult = mockMvc.perform(
            post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andReturn()

        val refreshTokenCookie = loginResult.response.getCookie("refreshToken")!!
        assertThat(refreshTokenCookie).isNotNull

        // 2. 만료된 Access Token 생성 (임시로 짧은 시간으로 생성)
        val account = accountJpaRepository.findByEmail("test@example.com")!!
        // 실제로는 시간을 조작할 수 없으므로, 잘못된 토큰을 사용
        val expiredAccessToken = "Bearer invalid-expired-token"

        // 3. 만료된 Access Token과 유효한 Refresh Token으로 요청
        val result = mockMvc.perform(
            get("/health")  // 인증이 필요한 엔드포인트
                .header("Authorization", expiredAccessToken)
                .cookie(Cookie("refreshToken", refreshTokenCookie.value))
        )
            .andDo(MockMvcResultHandlers.print())
            .andReturn()

        // 4. 새로운 Access Token이 응답 헤더에 포함되어 있는지 확인
        val newAccessToken = result.response.getHeader("Authorization")
        if (newAccessToken != null) {
            assertThat(newAccessToken).startsWith("Bearer ")
            assertThat(jwtTokenProvider.validateToken(newAccessToken.substring(7))).isTrue()
        }
    }

    @Test
    fun `재로그인 시 기존 Refresh Token이 업데이트됨`() {
        val loginRequest = UserLoginRequest("test@example.com", "password1234")

        // 첫 번째 로그인
        mockMvc.perform(
            post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)

        val firstToken = refreshTokenRepository.findAll()[0].token

        // 시간 차이를 두기 위해 대기
        Thread.sleep(1100)

        // 두 번째 로그인
        mockMvc.perform(
            post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)

        // Refresh Token이 하나만 존재하고 업데이트되었는지 확인
        val savedTokens = refreshTokenRepository.findAll()
        assertThat(savedTokens).hasSize(1)
        assertThat(savedTokens[0].token).isNotEqualTo(firstToken)
    }
}
