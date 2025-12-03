package ac.dnd.server.account.infrastructure.security

import ac.dnd.server.account.domain.enums.Role
import ac.dnd.server.account.infrastructure.persistence.entity.Account
import ac.dnd.server.account.infrastructure.persistence.repository.AccountJpaRepository
import ac.dnd.server.account.infrastructure.security.dto.UserLoginRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Testcontainers
class LoginIntegrationTest {

    companion object {
        @Container
        @ServiceConnection
        val mysqlContainer = MySQLContainer<Nothing>("mysql:8.0").apply {
            withDatabaseName("testdb")
            withUsername("test")
            withPassword("test")
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("encryption.aes.password") { "testpassword" }
            registry.add("encryption.aes.salt") { "12345678" }
            registry.add("encryption.hmac.key") { "12345678" }
            registry.add("github.token") { "testtoken" }
            registry.add("github.repository.owner") { "testowner" }
            registry.add("github.repository.name") { "testname" }
            registry.add("jwt.secret") { "testSecretKeyForJwtTokenThatIsLongEnoughForHS256Algorithm" }
            registry.add("jwt.accessTokenExpirationHours") { "1" }
            registry.add("jwt.refreshTokenExpirationDays") { "30" }
        }
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var accountJpaRepository: AccountJpaRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        accountJpaRepository.deleteAll()
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
    fun `login success`() {
        val loginRequest = UserLoginRequest("test@example.com", "password1234")
        mockMvc.perform(
            post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("Login successful"))
    }

    @Test
    fun `login failure - wrong password`() {
        val loginRequest = UserLoginRequest("test@example.com", "wrongpassword")

        mockMvc.perform(
            post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.message").value("Login failed"))
    }

    @Test
    fun `login failure - user not found`() {
        val loginRequest = UserLoginRequest("unknown@example.com", "password1234")

        mockMvc.perform(
            post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.message").value("Login failed"))
    }
}
