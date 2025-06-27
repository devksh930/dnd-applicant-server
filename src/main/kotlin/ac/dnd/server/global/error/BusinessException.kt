package ac.dnd.server.global.error

import ac.dnd.server.global.support.ErrorCode

open class BusinessException : RuntimeException {
    val errorCode: ErrorCode

    constructor(message: String, errorCode: ErrorCode) : super(message) {
        this.errorCode = errorCode
    }

    constructor(errorCode: ErrorCode) : super(errorCode.message) {
        this.errorCode = errorCode
    }
}