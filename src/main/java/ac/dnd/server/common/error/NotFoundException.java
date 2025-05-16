package ac.dnd.server.common.error;

import ac.dnd.server.common.support.ErrorCode;

public class NotFoundException extends BusinessException {
	public NotFoundException(
		final String message,
		final ErrorCode errorCode
	) {
		super(
			message,
			errorCode
		);
	}

	public NotFoundException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
