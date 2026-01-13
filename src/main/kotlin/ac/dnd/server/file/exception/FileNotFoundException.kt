package ac.dnd.server.file.exception

import ac.dnd.server.common.exception.ErrorCode
import ac.dnd.server.common.exception.NotFoundException

class FileNotFoundException : NotFoundException(ErrorCode.FILE_NOT_FOUND)