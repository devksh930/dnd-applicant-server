package ac.dnd.server.review.exception

import ac.dnd.server.common.exception.ErrorCode
import ac.dnd.server.common.exception.NotFoundException

class ProjectNotFoundException : NotFoundException(ErrorCode.PROJECT_NOT_FOUND)