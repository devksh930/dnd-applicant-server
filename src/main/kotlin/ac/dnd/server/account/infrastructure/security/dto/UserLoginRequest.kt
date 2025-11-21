package ac.dnd.server.account.infrastructure.security.dto

data class UserLoginRequest(
    val email: String,
    val password: String
) {
}