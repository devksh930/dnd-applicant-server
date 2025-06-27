package ac.dnd.server.global.error

import ac.dnd.server.global.support.ErrorCode
import ac.dnd.server.global.error.FieldError
import org.springframework.validation.BindingResult
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

data class ErrorResponse(
    val message: String,
    val status: Int,
    val errors: List<FieldError>,
    val code: String
) {
    constructor(errorCode: ErrorCode, errors: List<FieldError>) : this(
        errorCode.message,
        errorCode.status,
        errors,
        errorCode.code
    )

    constructor(errorCode: ErrorCode) : this(
        errorCode.message,
        errorCode.status,
        emptyList(),
        errorCode.code
    )

    companion object {
        fun of(code: ErrorCode, bindingResult: BindingResult): ErrorResponse {
            return ErrorResponse(code, FieldError.of(bindingResult))
        }

        fun of(code: ErrorCode): ErrorResponse {
            return ErrorResponse(code)
        }

        fun of(code: ErrorCode, errors: List<FieldError>): ErrorResponse {
            return ErrorResponse(code, errors)
        }

        fun of(e: MethodArgumentTypeMismatchException): ErrorResponse {
            val value = e.value?.toString() ?: ""
            val fieldErrors = FieldError.of(e.name, value, e.errorCode)
            return ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, fieldErrors)
        }
    }
}
