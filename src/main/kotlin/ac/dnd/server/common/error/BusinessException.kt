package ac.dnd.server.common.error

import ac.dnd.server.common.support.ErrorCode

open class BusinessException : RuntimeException {
    val errorCode: ErrorCode

    constructor(message: String, errorCode: ErrorCode) : super(message) {
        this.errorCode = errorCode
    }

    constructor(errorCode: ErrorCode) : super(errorCode.message) {
        this.errorCode = errorCode
    }
}