package ac.dnd.server.account.infrastructure.security.authentication.handler

import ac.dnd.server.account.infrastructure.security.authentication.userdetails.AccountDetails
import ac.dnd.server.account.infrastructure.security.jwt.JwtTokenProvider
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class RestAuthSuccessHandler(
    private val objectMapper: ObjectMapper,
    private val jwtTokenProvider: JwtTokenProvider
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val loginUserInfo = extractLoginUserInfo(authentication)
        val tokens = generateTokens(loginUserInfo)

        setAuthenticationResponse(response, tokens)
        writeSuccessResponse(response)
    }

    private fun extractLoginUserInfo(authentication: Authentication): AccountDetails {
        return authentication.principal as AccountDetails
    }

    private fun generateTokens(accountDetails: AccountDetails): TokenPair {
        val role = accountDetails.authorities.first().authority.removePrefix("ROLE_")

        val accessToken = jwtTokenProvider.generateAccessToken(accountDetails.userKey, accountDetails.username, role)
        val refreshToken = jwtTokenProvider.generateRefreshToken(accountDetails.userKey)

        return TokenPair(accessToken, refreshToken)
    }

    private fun setAuthenticationResponse(response: HttpServletResponse, tokens: TokenPair) {
        response.setHeader("Authorization", "Bearer ${tokens.accessToken}")
        response.addCookie(createRefreshTokenCookie(tokens.refreshToken))
    }

    private fun createRefreshTokenCookie(refreshToken: String): Cookie {
        return Cookie("refreshToken", refreshToken).apply {
            isHttpOnly = true
            secure = false
            path = "/"
            maxAge = REFRESH_TOKEN_MAX_AGE
        }
    }

    private fun writeSuccessResponse(response: HttpServletResponse) {
        response.status = HttpStatus.OK.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"

        objectMapper.writeValue(response.writer, mapOf("message" to "Login successful"))
    }

    private data class TokenPair(
        val accessToken: String,
        val refreshToken: String
    )

    companion object {
        private const val REFRESH_TOKEN_MAX_AGE = 30 * 24 * 60 * 60
    }
}