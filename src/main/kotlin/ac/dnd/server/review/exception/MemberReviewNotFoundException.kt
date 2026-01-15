package ac.dnd.server.review.exception

import ac.dnd.server.common.exception.ErrorCode
import ac.dnd.server.common.exception.NotFoundException

class MemberReviewNotFoundException : NotFoundException(ErrorCode.MEMBER_REVIEW_NOT_FOUND)
