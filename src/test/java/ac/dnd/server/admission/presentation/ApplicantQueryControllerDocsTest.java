package ac.dnd.server.admission.presentation;

import static ac.dnd.server.documenation.DocumentFormatGenerator.*;
import static ac.dnd.server.documenation.DocumentUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ac.dnd.server.admission.application.dto.ApplicantData;
import ac.dnd.server.admission.application.dto.ApplicantStatusCheckCommand;
import ac.dnd.server.admission.application.service.ApplicantQueryService;
import ac.dnd.server.annotation.RestDocsTest;
import ac.dnd.server.documenation.MockMvcFactory;
import ac.dnd.server.enums.ApplicantStatus;
import ac.dnd.server.enums.ApplicantType;

@RestDocsTest
class ApplicantQueryControllerDocsTest {

	@Mock
	private ApplicantQueryService applicantQueryService;

	@InjectMocks
	private ApplicantQueryController controller;

	private static final String HOST_API = "api.dnd.ac"; // 예시 호스트 (실제 사용하는 호스트명으로 변경)

	@DisplayName("지원 상태 조회")
	@Test
	void checkApplicantStatusTest(RestDocumentationContextProvider contextProvider) throws Exception {
		var createRequest = """
			{
				"name":"홍길동",
				"email":"test@test.com"
			}
			""";
		var requestFiledDescription = new FieldDescriptor[] {
			fieldWithPath("name").type(STRING).description("이름"),
			fieldWithPath("email").type(STRING).description("이메일")
		};
		final ApplicantData validateQuery = new ApplicantData(
			"12",
			"홍길동",
			"test@test.com",
			ApplicantType.BACKEND,
			ApplicantStatus.PASSED
		);

		Mockito.when(applicantQueryService.getApplicantStatusCheck(any(ApplicantStatusCheckCommand.class)))
			.thenReturn(validateQuery);

		var responseFiledDescription = new FieldDescriptor[] {
			fieldWithPath("eventName").type(STRING).description("이벤트명"),
			fieldWithPath("name").type(STRING).description("지원자명"),
			fieldWithPath("status").type(STRING)
				.attributes(generateEnumAttrs(
					ApplicantStatus.class,
					ApplicantStatus::getDescription
				)).description("지원 상태 \n" + generatedEnums(
				ApplicantStatus.class
			))
		};

		MockMvcFactory.getRestDocsMockMvc(
				contextProvider,
				HOST_API,
				controller
			)
			.perform(RestDocumentationRequestBuilders.post(
						"/event/{eventId}/applicant/status/check",
						1
					)
					.contentType(MediaType.APPLICATION_JSON)
					.content(createRequest)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			//restDocs
			.andDo(MockMvcRestDocumentation.document(
				"get-applicant-status",
				getDocumentRequest(),
				getDocumentResponse(),
				pathParameters(
					RequestDocumentation.parameterWithName("eventId").description("이벤트 ID")
				),
				requestFields(requestFiledDescription),
				responseFields(responseFiledDescription)
			));
	}
}