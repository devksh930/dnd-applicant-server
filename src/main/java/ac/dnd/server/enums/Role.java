package ac.dnd.server.enums;

import lombok.Getter;

@Getter
public enum Role {
	ADMIN("관리자");

	private final String description;

	Role(final String description) {
		this.description = description;
	}
}
