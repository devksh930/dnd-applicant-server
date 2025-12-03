package ac.dnd.server.account.infrastructure.security.jwt

import ac.dnd.server.account.infrastructure.persistence.repository.AccountJpaRepository
import ac.dnd.server.account.infrastructure.persistence.repository.RefreshTokenRepository
import ac.dnd.server.shared.dto.LoginInfo
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val accountJpaRepository: AccountJpaRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val accessToken = extractBearerToken(request)

            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                val loginInfo = createLoginInfo(accessToken)
                setAuthentication(request, loginInfo, accessToken)
            } else if (accessToken != null) {
                val refreshToken = extractRefreshTokenFromCookie(request)
                if (refreshToken != null) {
                    tryRefreshAccessToken(request, response, refreshToken)
                }
            }
        } catch (e: Exception) {
            logger.error("Could not set user authentication in security context", e)
        }

        filterChain.doFilter(request, response)
    }

    private fun extractBearerToken(request: HttpServletRequest): String? {
        return request.getHeader(AUTHORIZATION_HEADER)
            ?.takeIf { it.startsWith(BEARER_PREFIX) }
            ?.substring(BEARER_PREFIX.length)
    }

    private fun createLoginInfo(token: String): LoginInfo {
        return LoginInfo(
            userId = jwtTokenProvider.extractUserKey(token)
        )
    }

    private fun extractRefreshTokenFromCookie(request: HttpServletRequest): String? {
        return request.cookies
            ?.firstOrNull { it.name == REFRESH_TOKEN_COOKIE_NAME }
            ?.value
    }

    private fun tryRefreshAccessToken(
        request: HttpServletRequest,
        response: HttpServletResponse,
        refreshToken: String
    ) {
        try {
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                return
            }
            val storedToken = refreshTokenRepository.findByToken(refreshToken) ?: return
            if (storedToken.isExpired()) {
                return
            }

            val account = accountJpaRepository.findByUserKey(storedToken.userKey) ?: return
            val newAccessToken = jwtTokenProvider.generateAccessToken(
                userKey = account.userKey.value,
                email = account.email,
                role = account.role.name
            )

            response.setHeader(AUTHORIZATION_HEADER, "$BEARER_PREFIX$newAccessToken")

            val loginInfo = LoginInfo(userId = account.userKey.value)
            setAuthentication(request, loginInfo, newAccessToken)

        } catch (e: Exception) {
            logger.error("Failed to refresh access token", e)
        }
    }

    private fun setAuthentication(request: HttpServletRequest, loginInfo: LoginInfo, token: String) {
        val authorities = listOf(
            SimpleGrantedAuthority("ROLE_${jwtTokenProvider.extractRole(token)}")
        )

        val authentication = UsernamePasswordAuthenticationToken(
            loginInfo,
            null,
            authorities
        ).apply {
            details = WebAuthenticationDetailsSource().buildDetails(request)
        }

        SecurityContextHolder.getContext().authentication = authentication
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
        private const val REFRESH_TOKEN_COOKIE_NAME = "refreshToken"
    }
}
