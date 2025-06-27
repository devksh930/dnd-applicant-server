package ac.dnd.server.global.error

import ac.dnd.server.global.support.ErrorCode

open class NotFoundException : BusinessException {
    constructor(message: String, errorCode: ErrorCode) : super(message, errorCode)
    
    constructor(errorCode: ErrorCode) : super(errorCode)
}