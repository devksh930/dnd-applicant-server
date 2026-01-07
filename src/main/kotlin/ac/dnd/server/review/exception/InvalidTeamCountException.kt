package ac.dnd.server.review.exception

import ac.dnd.server.common.exception.BusinessException
import ac.dnd.server.common.exception.ErrorCode

class InvalidTeamCountException : BusinessException(ErrorCode.INVALID_TEAM_COUNT)
