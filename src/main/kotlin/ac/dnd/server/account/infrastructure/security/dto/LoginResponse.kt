package ac.dnd.server.account.infrastructure.security.dto

data class LoginResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long
)