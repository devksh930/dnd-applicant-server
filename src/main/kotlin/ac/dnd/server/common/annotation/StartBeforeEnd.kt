package ac.dnd.server.common.annotation

import ac.dnd.server.common.validator.StartBeforeEndValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [StartBeforeEndValidator::class])
annotation class StartBeforeEnd(
    val message: String = "시작 시간은 종료 시간보다 이전이어야 합니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val startDateTime: String,
    val endDateTime: String
)