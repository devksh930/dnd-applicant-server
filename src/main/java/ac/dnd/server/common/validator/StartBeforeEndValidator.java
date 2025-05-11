package ac.dnd.server.common.validator;

import java.time.LocalDateTime;

import org.springframework.beans.BeanWrapperImpl;

import ac.dnd.server.common.annotation.StartBeforeEnd;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, Object> {

	private String startDateTimeFiledName;
	private String endDateTimeFiledName;

	@Override
	public void initialize(final StartBeforeEnd constraintAnnotation) {
		this.startDateTimeFiledName = constraintAnnotation.startDateTime();
		this.endDateTimeFiledName = constraintAnnotation.endDateTime();
	}

	@Override
	public boolean isValid(
		final Object value,
		final ConstraintValidatorContext context
	) {
		if (value == null) {
			return true;
		}
		LocalDateTime startDateTime = getLocalDateTimeProperty(
			value,
			startDateTimeFiledName
		);
		LocalDateTime endDateTime = getLocalDateTimeProperty(
			value,
			endDateTimeFiledName
		);

		if (startDateTime == null || endDateTime == null) {
			return true;
		}

		boolean isValid = startDateTime.isBefore(endDateTime);

		if (!isValid) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
				.addPropertyNode(endDateTimeFiledName)
				.addConstraintViolation();
		}

		return isValid;
	}

	private LocalDateTime getLocalDateTimeProperty(
		final Object value,
		final String fieldName
	) {
		Object propertyValue = new BeanWrapperImpl(value).getPropertyValue(fieldName);

		if (propertyValue == null) {
			return null;
		}

		if (!(propertyValue instanceof LocalDateTime)) {
			throw new IllegalArgumentException(String.format(
				"필드 '%s'의 타입이 LocalDateTime이어야 합니다.",
				fieldName
			));
		}
		return (LocalDateTime)propertyValue;
	}

}
