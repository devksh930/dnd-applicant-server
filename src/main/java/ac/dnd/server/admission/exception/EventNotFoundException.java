package ac.dnd.server.admission.exception;

import ac.dnd.server.common.error.NotFoundException;
import ac.dnd.server.common.support.ErrorCode;

public class EventNotFoundException extends NotFoundException {
	public EventNotFoundException() {
		super(ErrorCode.EVENT_NOT_FOUND);
	}
}
