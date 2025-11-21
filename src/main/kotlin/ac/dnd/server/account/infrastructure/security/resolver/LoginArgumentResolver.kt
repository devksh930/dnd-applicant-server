package ac.dnd.server.account.infrastructure.security.resolver

import ac.dnd.server.common.exception.ErrorCode
import ac.dnd.server.common.exception.UnauthenticatedException
import ac.dnd.server.shared.annotation.AuthUser
import ac.dnd.server.shared.dto.LoginInfo
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class LoginArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(AuthUser::class.java) &&
                parameter.parameterType == LoginInfo::class.java


    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): LoginInfo {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw UnauthenticatedException(ErrorCode.INVALID_AUTH_INFO)

        if (!authentication.isAuthenticated) {
            throw UnauthenticatedException(ErrorCode.INVALID_AUTH_INFO)
        }

        return when (val principal = authentication.principal) {
            is LoginInfo -> principal
            else -> throw UnauthenticatedException(ErrorCode.INVALID_AUTH_INFO)
        }
    }
}