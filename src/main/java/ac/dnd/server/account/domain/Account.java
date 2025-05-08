package ac.dnd.server.account.domain;

import ac.dnd.server.common.BaseEntity;
import ac.dnd.server.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {
	private String name;
	private String email;
	private String password;
	private Role role;

	public Account(
		final String name,
		final String email,
		final String password,
		final Role role
	) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
	}
}
