package ac.dnd.server.admission.exception

import ac.dnd.server.global.error.BusinessException
import ac.dnd.server.global.support.ErrorCode

class ApplicantViewablePeriodEndException : BusinessException(ErrorCode.APPLICANT_VIEWABLE_PERIOD_END)