package ac.dnd.server.admission.exception

import ac.dnd.server.common.exception.BusinessException
import ac.dnd.server.common.exception.ErrorCode

class ApplicantViewablePeriodEndException : BusinessException(ErrorCode.APPLICANT_VIEWABLE_PERIOD_END)
