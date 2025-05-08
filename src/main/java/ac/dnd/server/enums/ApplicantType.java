package ac.dnd.server.enums;

import lombok.Getter;

@Getter
public enum ApplicantType {
	FRONTEND("프론트엔드"),
	BACKEND("백엔드"),
	DESIGNER("디자이너");

	private final String description;

	ApplicantType(final String description) {
		this.description = description;
	}
}
