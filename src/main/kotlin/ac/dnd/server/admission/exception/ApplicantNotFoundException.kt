package ac.dnd.server.admission.exception

import ac.dnd.server.common.error.NotFoundException
import ac.dnd.server.common.support.ErrorCode

class ApplicantNotFoundException : NotFoundException(ErrorCode.APPLICANT_NOT_FOUND)