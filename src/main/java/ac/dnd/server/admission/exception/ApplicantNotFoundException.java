package ac.dnd.server.admission.exception;

import ac.dnd.server.common.error.NotFoundException;
import ac.dnd.server.common.support.ErrorCode;

public class ApplicantNotFoundException extends NotFoundException {

	public ApplicantNotFoundException() {
		super(ErrorCode.APPLICANT_NOT_FOUND);
	}
}
