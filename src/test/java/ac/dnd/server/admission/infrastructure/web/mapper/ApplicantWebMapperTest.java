package ac.dnd.server.admission.infrastructure.web.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ac.dnd.server.admission.application.dto.ApplicantStatusCheckCommand;
import ac.dnd.server.admission.infrastructure.web.dto.request.ApplicantStatusCheckRequest;
import ac.dnd.server.annotation.UnitTest;
import ac.dnd.server.fixture.TextEncryptorFixture;

@UnitTest
class ApplicantWebMapperTest {

	private ApplicantWebMapper applicantWebMapper = new ApplicantWebMapper(TextEncryptorFixture.createNoOpTextEncryptor());

	@Test
	void testStatusCheckRequestToCommand() {

		ApplicantStatusCheckCommand result = applicantWebMapper.statusCheckRequestToCommand(
			new ApplicantStatusCheckRequest(
				"name",
				"email"
			),
			Long.valueOf(1)
		);

		Assertions.assertEquals(
			new ApplicantStatusCheckCommand(
				Long.valueOf(1),
				"name",
				"email"
			),
			result
		);
	}
}

