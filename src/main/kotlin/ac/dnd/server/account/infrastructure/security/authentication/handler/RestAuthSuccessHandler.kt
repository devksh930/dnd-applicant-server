package ac.dnd.server.account.infrastructure.security.authentication.handler

import ac.dnd.server.account.infrastructure.persistence.entity.RefreshToken
import ac.dnd.server.account.infrastructure.persistence.entity.UserKey
import ac.dnd.server.account.infrastructure.persistence.repository.RefreshTokenRepository
import ac.dnd.server.account.infrastructure.security.authentication.userdetails.AccountDetails
import ac.dnd.server.account.infrastructure.security.dto.LoginResponse
import ac.dnd.server.account.infrastructure.security.jwt.JwtTokenProvider
import ac.dnd.server.shared.config.properties.JwtProperties
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class RestAuthSuccessHandler(
    private val objectMapper: ObjectMapper,
    private val jwtTokenProvider: JwtTokenProvider,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtProperties: JwtProperties
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val loginUserInfo = extractLoginUserInfo(authentication)
        val tokens = generateTokens(loginUserInfo)
        saveRefreshToken(loginUserInfo.userKey, tokens.refreshToken)

        setAuthenticationResponse(response, tokens)
        writeSuccessResponse(response, tokens.accessToken)
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
        response.addCookie(createRefreshTokenCookie(tokens.refreshToken))
    }

    private fun createRefreshTokenCookie(refreshToken: String): Cookie {
        return Cookie("refreshToken", refreshToken).apply {
            isHttpOnly = true
            secure = jwtProperties.cookie.secure
            path = "/"
            maxAge = jwtProperties.getRefreshTokenMaxAgeSeconds()
            setAttribute("SameSite", jwtProperties.cookie.sameSite)
            jwtProperties.cookie.domain?.takeIf { it.isNotBlank() }?.let { domain = it }
        }
    }

    private fun saveRefreshToken(userKey: java.util.UUID, refreshToken: String) {
        val expiresAt = LocalDateTime.now().plusDays(jwtProperties.refreshTokenExpirationDays)
        refreshTokenRepository.findByUserKey(UserKey(userKey))
            ?.apply { updateToken(refreshToken, expiresAt) }
            ?: refreshTokenRepository.save(RefreshToken(UserKey(userKey), refreshToken, expiresAt))
    }

    private fun writeSuccessResponse(response: HttpServletResponse, accessToken: String) {
        response.status = HttpStatus.OK.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")

        val loginResponse = LoginResponse(
            accessToken = accessToken,
            expiresIn = jwtProperties.accessTokenExpirationHours * 3600
        )
        objectMapper.writeValue(response.writer, loginResponse)
    }

    private data class TokenPair(
        val accessToken: String,
        val refreshToken: String
    )
}