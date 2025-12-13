package ac.dnd.server.account.infrastructure.security

import ac.dnd.server.account.domain.enums.Role
import ac.dnd.server.account.infrastructure.persistence.entity.Account
import ac.dnd.server.account.infrastructure.persistence.repository.AccountJpaRepository
import ac.dnd.server.account.infrastructure.security.dto.UserLoginRequest
import ac.dnd.server.annotation.IntegrationTest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@IntegrationTest
@AutoConfigureMockMvc
@Transactional
class LoginIntegrationTest {

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
            .andExpect(header().exists("Authorization"))
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
