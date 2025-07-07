package ac.dnd.server.shared.web

import ac.dnd.server.common.exception.BusinessException
import ac.dnd.server.common.exception.ErrorCode
import ac.dnd.server.common.exception.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotValidException(
        e: MethodArgumentNotValidException
    ): ResponseEntity<ErrorResponse> {
        log.error("handleMethodArgumentNotValidException", e)
        val response = ErrorResponse.of(
            ErrorCode.INVALID_INPUT_VALUE,
            e.bindingResult
        )
        return ResponseEntity.status(response.status).body(response)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    protected fun handleHttpRequestMethodNotSupportedException(
        e: HttpRequestMethodNotSupportedException
    ): ResponseEntity<ErrorResponse> {
        log.error("handleHttpRequestMethodNotSupportedException", e)
        val response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED)
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response)
    }

    @ExceptionHandler(Exception::class)
    protected fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        log.error("handleException", e)
        val response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR)
        return ResponseEntity.status(response.status).body(response)
    }

    @ExceptionHandler(BusinessException::class)
    protected fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        log.error("handleBusinessException", e)
        val errorCode = e.errorCode
        val response = ErrorResponse.of(errorCode)
        return ResponseEntity.status(response.status).body(response)
    }

}
