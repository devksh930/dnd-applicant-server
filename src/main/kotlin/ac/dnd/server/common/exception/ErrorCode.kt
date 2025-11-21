package ac.dnd.server.common.exception

enum class ErrorCode(
    val status: Int,
    val code: String,
    val message: String
) {
    // Common
    INVALID_INPUT_VALUE(
        422,
        "C001",
        "Invalid Input Value"
    ),
    METHOD_NOT_ALLOWED(
        405,
        "C002",
        "Method Not Allowed"
    ),
    ENTITY_NOT_FOUND(
        404,
        "C003",
        " Entity Not Found"
    ),
    INTERNAL_SERVER_ERROR(
        500,
        "C004",
        "Server Error"
    ),
    INVALID_TYPE_VALUE(
        400,
        "C005",
        " Invalid Type Value"
    ),
    HANDLE_ACCESS_DENIED(
        403,
        "C006",
        "Access is Denied"
    ),
    APPLICANT_NOT_FOUND(
        404,
        "A001",
        "이름과 이메일을 다시 확인해주세요."
    ),
    APPLICANT_VIEWABLE_PERIOD_END(
        422,
        "A002",
        "합격자 조회 가능 기간이 지났습니다"
    ),
    EVENT_NOT_FOUND(
        404,
        "E001",
        "이벤트를 찾을수 없습니다."
    ),
    USER_NOT_FOUND(
        401,
        "A001",
        "존재하지 않는 계정입니다"
    ),
    UNAUTHENTICATED(
        403,
        "A002",
        "인증되지 않은 사용자입니다"
    ),
    INVALID_AUTH_INFO(
        401,
        "A003",
        "올바르지 않은 인증 정보입니다"
    ),

}
