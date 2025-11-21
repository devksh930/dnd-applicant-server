package ac.dnd.server.account.infrastructure.security.jwt

import ac.dnd.server.account.infrastructure.security.authentication.userdetails.AccountDetails
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            extractBearerToken(request)
                ?.takeIf { jwtTokenProvider.validateToken(it) }
                ?.let { token ->
                    val loginUserInfo = createLoginUserInfo(token)
                    setAuthentication(request, loginUserInfo)
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

    private fun createLoginUserInfo(token: String): AccountDetails {
        return AccountDetails(
            userKey = jwtTokenProvider.extractUserKey(token),
            email = jwtTokenProvider.extractEmail(token),
            password = "",
            authorities = listOf(
                SimpleGrantedAuthority("ROLE_${jwtTokenProvider.extractRole(token)}")
            )
        )
    }

    private fun setAuthentication(request: HttpServletRequest, accountDetails: AccountDetails) {
        val authentication = UsernamePasswordAuthenticationToken(
            accountDetails,
            null,
            accountDetails.authorities
        ).apply {
            details = WebAuthenticationDetailsSource().buildDetails(request)
        }

        SecurityContextHolder.getContext().authentication = authentication
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
    }
}
