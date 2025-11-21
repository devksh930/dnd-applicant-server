package ac.dnd.server.shared.annotation

@Target(AnnotationTarget.VALUE_PARAMETER) // 파라미터에만 붙일 수 있음
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthUser {
}