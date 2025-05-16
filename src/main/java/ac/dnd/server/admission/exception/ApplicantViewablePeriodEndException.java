package ac.dnd.server.admission.exception;

import ac.dnd.server.common.error.BusinessException;
import ac.dnd.server.common.support.ErrorCode;

public class ApplicantViewablePeriodEndException extends BusinessException {
	public ApplicantViewablePeriodEndException() {
		super(ErrorCode.APPLICANT_VIEWABLE_PERIOD_END);
	}
}
