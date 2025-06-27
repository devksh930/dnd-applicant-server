package ac.dnd.server.global.validator

import ac.dnd.server.global.annotation.StartBeforeEnd
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.beans.BeanWrapperImpl
import java.time.LocalDateTime

class StartBeforeEndValidator : ConstraintValidator<StartBeforeEnd, Any> {

    private lateinit var startDateTimeFieldName: String
    private lateinit var endDateTimeFieldName: String

    override fun initialize(constraintAnnotation: StartBeforeEnd) {
        this.startDateTimeFieldName = constraintAnnotation.startDateTime
        this.endDateTimeFieldName = constraintAnnotation.endDateTime
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }

        val startDateTime = getLocalDateTimeProperty(value, startDateTimeFieldName)
        val endDateTime = getLocalDateTimeProperty(value, endDateTimeFieldName)

        if (startDateTime == null || endDateTime == null) {
            return true
        }

        val isValid = startDateTime.isBefore(endDateTime)

        if (!isValid) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate(context.defaultConstraintMessageTemplate)
                .addPropertyNode(endDateTimeFieldName)
                .addConstraintViolation()
        }

        return isValid
    }

    private fun getLocalDateTimeProperty(value: Any, fieldName: String): LocalDateTime? {
        val propertyValue = BeanWrapperImpl(value).getPropertyValue(fieldName)

        if (propertyValue == null) {
            return null
        }

        if (propertyValue !is LocalDateTime) {
            throw IllegalArgumentException("필드 '$fieldName'의 타입이 LocalDateTime이어야 합니다.")
        }

        return propertyValue
    }
}