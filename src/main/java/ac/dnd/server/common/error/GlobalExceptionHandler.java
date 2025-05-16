package ac.dnd.server.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ac.dnd.server.common.support.ErrorCode;
import ac.dnd.server.common.support.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e
	) {
		log.error(
			"handleMethodArgumentNotValidException",
			e
		);
		final ErrorResponse response = ErrorResponse.of(
			ErrorCode.INVALID_INPUT_VALUE,
			e.getBindingResult()
		);
		return ResponseEntity.status(response.status()).body(response);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
		HttpRequestMethodNotSupportedException e
	) {
		log.error(
			"handleHttpRequestMethodNotSupportedException",
			e
		);
		final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error(
			"handleException",
			e
		);
		final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
		return ResponseEntity.status(response.status()).body(response);
	}

	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
		log.error(
			"handleBusinessException",
			e
		);
		final ErrorCode errorCode = e.getErrorCode();
		final ErrorResponse response = ErrorResponse.of(errorCode);
		return ResponseEntity.status(response.status()).body(response);
	}

}
