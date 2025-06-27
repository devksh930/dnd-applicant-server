package ac.dnd.server.admission.exception

import ac.dnd.server.global.error.NotFoundException
import ac.dnd.server.global.support.ErrorCode

class EventNotFoundException : NotFoundException(ErrorCode.EVENT_NOT_FOUND)