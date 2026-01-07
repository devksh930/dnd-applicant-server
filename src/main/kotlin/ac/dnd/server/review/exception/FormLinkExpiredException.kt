package ac.dnd.server.review.exception

import ac.dnd.server.common.exception.BusinessException
import ac.dnd.server.common.exception.ErrorCode

class FormLinkExpiredException : BusinessException(ErrorCode.FORM_LINK_EXPIRED)
