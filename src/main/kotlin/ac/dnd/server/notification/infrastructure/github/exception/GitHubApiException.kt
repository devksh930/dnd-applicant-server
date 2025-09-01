package ac.dnd.server.notification.infrastructure.github.exception

import ac.dnd.server.common.exception.BusinessException
import ac.dnd.server.common.exception.ErrorCode

class GitHubApiException(message: String) : BusinessException(message, ErrorCode.INTERNAL_SERVER_ERROR)
