package ac.dnd.server.common.support;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public record ErrorResponse(
	String message,
	int status,
	List<FieldError> errors,
	String code
) {

	public ErrorResponse(
		final ErrorCode errorCode,
		final List<FieldError> errors
	) {
		this(
			errorCode.getMessage(),
			errorCode.getStatus(),
			errors,
			errorCode.getCode()
		);
	}

	public ErrorResponse(final ErrorCode errorCode) {
		this(
			errorCode.getMessage(),
			errorCode.getStatus(),
			Collections.emptyList(),
			errorCode.getCode()
		);
	}

	public static ErrorResponse of(
		final ErrorCode code,
		final BindingResult bindingResult
	) {
		return new ErrorResponse(
			code,
			FieldError.of(bindingResult)
		);
	}

	public static ErrorResponse of(final ErrorCode code) {
		return new ErrorResponse(code);
	}

	public static ErrorResponse of(
		final ErrorCode code,
		final List<FieldError> errors
	) {
		return new ErrorResponse(
			code,
			errors
		);
	}

	public static ErrorResponse of(final MethodArgumentTypeMismatchException e) {
		final String value = StringUtils.defaultIfEmpty(
			e.getValue().toString(),
			""
		);
		final List<FieldError> fieldErrors = FieldError.of(
			e.getName(),
			value,
			e.getErrorCode()
		);
		return new ErrorResponse(
			ErrorCode.INVALID_TYPE_VALUE,
			fieldErrors
		);
	}
}