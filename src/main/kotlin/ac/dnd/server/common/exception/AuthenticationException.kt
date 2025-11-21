package ac.dnd.server.common.exception

abstract class AuthenticationException(
    val errorCode: ErrorCode,
    message: String = errorCode.message,
    cause: Throwable? = null
) : RuntimeException(message, cause)

/**
 * 사용자를 찾을 수 없는 예외
 */
class UserNotFoundException(
    errorCode: ErrorCode = ErrorCode.USER_NOT_FOUND,
    message: String = errorCode.message,
    cause: Throwable? = null
) : AuthenticationException(errorCode, message, cause)

/**
 * 인증되지 않은 사용자 예외
 */
class UnauthenticatedException(
    errorCode: ErrorCode = ErrorCode.UNAUTHENTICATED,
    message: String = errorCode.message,
    cause: Throwable? = null
) : AuthenticationException(errorCode, message, cause)
