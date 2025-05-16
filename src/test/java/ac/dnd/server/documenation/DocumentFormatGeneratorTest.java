package ac.dnd.server.documenation;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ac.dnd.server.enums.ApplicantStatus;

/**
 * enum 타입 아스키독 목록 스니펫 사용 문자형식 생성
 */
@DisplayName("지정된 enum 타입을 아스키독문법 목록(<li>) 형태로 출력")
class DocumentFormatGeneratorTest {
	@DisplayName("속성값만 나열함")
	@Test
	void testGeneratedEnums() {
		var result = DocumentFormatGenerator.generatedEnums(ApplicantStatus.class);

		assertThat(result).isEqualTo("""
			* `NONE`
			* `PASSED`
			* `FAILED`
			* `WAITLISTED`"""
		);
	}

	@DisplayName("속성값(설명) 나열함")
	@Test
	void testGeneratedEnumAttr() {
		var attrs = DocumentFormatGenerator.generateEnumAttrs(
			ApplicantStatus.class,
			ApplicantStatus::getDescription
		);

		assertThat(attrs.getValue()).isEqualTo("""
			* `NONE`(미정)
			* `PASSED`(합격)
			* `FAILED`(불합격)
			* `WAITLISTED`(예비 후보자)"""
		);
	}
}