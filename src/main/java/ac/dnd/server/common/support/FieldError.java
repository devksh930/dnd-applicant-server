package ac.dnd.server.common.support;

import java.util.List;

import org.springframework.validation.BindingResult;

public record FieldError(
	String field,
	String value,
	String reason
) {
	public static List<FieldError> of(
		final String field,
		final String value,
		final String reason
	) {
		return List.of(new FieldError(
			field,
			value,
			reason
		));
	}

	public static List<FieldError> of(
		final BindingResult bindingResult
	) {
		final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
		return fieldErrors.stream()
			.map(error -> new FieldError(
				error.getField(),
				error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
				error.getDefaultMessage()
			))
			.toList();
	}
}