package ac.dnd.server.account.infrastructure.web

import ac.dnd.server.account.infrastructure.persistence.entity.UserKey
import ac.dnd.server.account.infrastructure.persistence.repository.RefreshTokenRepository
import ac.dnd.server.shared.annotation.AuthUser
import ac.dnd.server.shared.dto.LoginInfo
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val refreshTokenRepository: RefreshTokenRepository
) {

    @PostMapping("/logout")
    @Transactional
    fun logout(
        @AuthUser loginInfo: LoginInfo,
        response: HttpServletResponse
    ): ResponseEntity<Map<String, String>> {
        refreshTokenRepository.deleteByUserKey(UserKey(loginInfo.userId))

        response.addCookie(createExpiredRefreshTokenCookie())

        return ResponseEntity.ok(mapOf("message" to "Logout successful"))
    }

    private fun createExpiredRefreshTokenCookie(): Cookie {
        return Cookie("refreshToken", "").apply {
            isHttpOnly = true
            secure = false
            path = "/"
            maxAge = 0
        }
    }
}
