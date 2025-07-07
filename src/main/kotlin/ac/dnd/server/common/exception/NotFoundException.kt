package ac.dnd.server.common.exception

open class NotFoundException : BusinessException {
    constructor(message: String, errorCode: ErrorCode) : super(message, errorCode)

    constructor(errorCode: ErrorCode) : super(errorCode)
}
