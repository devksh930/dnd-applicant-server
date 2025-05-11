package ac.dnd.server.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ac.dnd.server.common.validator.StartBeforeEndValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartBeforeEndValidator.class)
public @interface StartBeforeEnd {
	String message() default "시작 시간은 종료 시간보다 이전이어야 합니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String startDateTime();

	String endDateTime();
}
