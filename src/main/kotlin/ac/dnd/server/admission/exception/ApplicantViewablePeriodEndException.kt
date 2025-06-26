package ac.dnd.server.admission.exception

import ac.dnd.server.common.error.BusinessException
import ac.dnd.server.common.support.ErrorCode

class ApplicantViewablePeriodEndException : BusinessException(ErrorCode.APPLICANT_VIEWABLE_PERIOD_END)