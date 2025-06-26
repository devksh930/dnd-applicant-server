package ac.dnd.server.admission.exception

import ac.dnd.server.common.error.NotFoundException
import ac.dnd.server.common.support.ErrorCode

class EventNotFoundException : NotFoundException(ErrorCode.EVENT_NOT_FOUND)