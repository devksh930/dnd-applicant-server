package ac.dnd.server.admission.domain.model;

import io.micrometer.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicantNameEmailBlindIndex {
	private String name;
	private String email;

	private ApplicantNameEmailBlindIndex(
		final String name,
		final String email
	) {

		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("시작일은 null일 수 없습니다.");
		}

		if (StringUtils.isEmpty(email)) {
			throw new IllegalArgumentException("시작일은 null일 수 없습니다.");
		}

		this.email = email;
		this.name = name;
	}

	public static ApplicantNameEmailBlindIndex of(
		final String name,
		final String email
	) {
		return new ApplicantNameEmailBlindIndex(
			name,
			email
		);
	}

}
