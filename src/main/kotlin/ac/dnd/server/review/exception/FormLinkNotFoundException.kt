package ac.dnd.server.review.exception

import ac.dnd.server.common.exception.ErrorCode
import ac.dnd.server.common.exception.NotFoundException

/**
 * 폼 링크를 찾을 수 없을 때 사용하는 예외.
 * 에러 코드는 PJ003(FORM_LINK_NOT_FOUND)로 매핑됩니다.
 */
class FormLinkNotFoundException : NotFoundException(ErrorCode.FORM_LINK_NOT_FOUND)
