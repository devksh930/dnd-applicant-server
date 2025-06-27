package ac.dnd.server.admission.exception

import ac.dnd.server.global.error.NotFoundException
import ac.dnd.server.global.support.ErrorCode

class ApplicantNotFoundException : NotFoundException(ErrorCode.APPLICANT_NOT_FOUND)