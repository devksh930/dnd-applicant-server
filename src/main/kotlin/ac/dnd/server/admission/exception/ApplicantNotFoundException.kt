package ac.dnd.server.admission.exception

import ac.dnd.server.common.exception.NotFoundException
import ac.dnd.server.common.exception.ErrorCode

class ApplicantNotFoundException : NotFoundException(ErrorCode.APPLICANT_NOT_FOUND)
