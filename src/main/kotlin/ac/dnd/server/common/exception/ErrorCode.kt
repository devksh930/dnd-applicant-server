package ac.dnd.server.common.exception

enum class ErrorCode(
    val status: Int,
    val code: String,
    val message: String
) {
    // Common (C)
    INVALID_INPUT_VALUE(
        422,
        "C001",
        "잘못된 입력값입니다"
    ),
    METHOD_NOT_ALLOWED(
        405,
        "C002",
        "허용되지 않은 메서드입니다"
    ),
    ENTITY_NOT_FOUND(
        404,
        "C003",
        "리소스를 찾을 수 없습니다"
    ),
    INTERNAL_SERVER_ERROR(
        500,
        "C004",
        "서버 오류가 발생했습니다"
    ),
    INVALID_TYPE_VALUE(
        400,
        "C005",
        "잘못된 타입입니다"
    ),
    HANDLE_ACCESS_DENIED(
        403,
        "C006",
        "접근이 거부되었습니다"
    ),
    // Applicant (AP)
    APPLICANT_NOT_FOUND(
        404,
        "AP001",
        "이름과 이메일을 다시 확인해주세요."
    ),
    APPLICANT_VIEWABLE_PERIOD_END(
        422,
        "AP002",
        "합격자 조회 가능 기간이 아닙니다"
    ),

    // Event (EV)
    EVENT_NOT_FOUND(
        404,
        "EV001",
        "이벤트를 찾을 수 없습니다."
    ),

    // Auth (AU)
    USER_NOT_FOUND(
        401,
        "AU001",
        "존재하지 않는 계정입니다"
    ),
    UNAUTHENTICATED(
        403,
        "AU002",
        "인증되지 않은 사용자입니다"
    ),
    INVALID_AUTH_INFO(
        401,
        "AU003",
        "올바르지 않은 인증 정보입니다"
    ),

    // Project (PJ)
    PROJECT_NOT_FOUND(
        404,
        "PJ001",
        "프로젝트를 찾을 수 없습니다"
    ),
    FORM_LINK_EXPIRED(
        422,
        "PJ002",
        "링크가 만료되었습니다"
    ),
    FORM_LINK_NOT_FOUND(
        404,
        "PJ003",
        "링크를 찾을 수 없습니다"
    ),
    INVALID_TEAM_COUNT(
        400,
        "PJ004",
        "팀 수는 1 이상이어야 합니다"
    ),

    // File (FL)
    FILE_NOT_FOUND(
        404,
        "FL001",
        "파일을 찾을 수 없습니다"
    ),
    FILE_UPLOAD_FAILED(
        500,
        "FL002",
        "파일 업로드에 실패했습니다"
    ),
}
