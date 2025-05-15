package ac.dnd.server.enums;

import lombok.Getter;

@Getter
public enum ApplicantStatus {
	NONE("미정"),
	PASSED("합격"),
	FAILED("불합격"),
	WAITLISTED("예비 후보자");

	private String description;

	ApplicantStatus(String description) {
		this.description = description;
	}

}