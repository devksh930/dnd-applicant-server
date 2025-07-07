package ac.dnd.server.admission.exception

import ac.dnd.server.common.exception.NotFoundException
import ac.dnd.server.common.exception.ErrorCode

class EventNotFoundException : NotFoundException(ErrorCode.EVENT_NOT_FOUND)
