package ac.dnd.server.account.infrastructure.security.authentication.filter

import ac.dnd.server.account.infrastructure.security.dto.UserLoginRequest
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter

class JsonUsernamePasswordAuthenticationFilter(
    private val objectMapper: ObjectMapper
) : AbstractAuthenticationProcessingFilter("/auth") {
    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Authentication {
        if (request.contentType == null || !request.contentType.contains("application/json")) {
            throw AuthenticationServiceException("Authentication content-type not supported: ${request.contentType}")
        }

        val request = objectMapper.readValue(request.inputStream, UserLoginRequest::class.java)
        val username = request.email
        val password = request.password

        if (username.isNullOrBlank() || password.isNullOrBlank()) {
            throw AuthenticationServiceException("Username or Password not provided")
        }

        val authRequest = UsernamePasswordAuthenticationToken(username, password)
        return this.authenticationManager.authenticate(authRequest)
    }
}