package ac.dnd.server.common.error

import ac.dnd.server.common.support.ErrorCode

open class NotFoundException : BusinessException {
    constructor(message: String, errorCode: ErrorCode) : super(message, errorCode)
    
    constructor(errorCode: ErrorCode) : super(errorCode)
}