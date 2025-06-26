package ac.dnd.server.common.support

import org.springframework.validation.BindingResult

data class FieldError(
    val field: String,
    val value: String,
    val reason: String
) {
    companion object {
        fun of(field: String, value: String, reason: String): List<FieldError> {
            return listOf(FieldError(field, value, reason))
        }

        fun of(bindingResult: BindingResult): List<FieldError> {
            val fieldErrors = bindingResult.fieldErrors
            return fieldErrors.map { error ->
                FieldError(
                    error.field,
                    error.rejectedValue?.toString() ?: "",
                    error.defaultMessage ?: ""
                )
            }
        }
    }
}