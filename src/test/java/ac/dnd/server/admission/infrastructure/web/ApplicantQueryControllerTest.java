package ac.dnd.server.admission.infrastructure.web;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import ac.dnd.server.admission.application.service.ApplicantQueryService;
import ac.dnd.server.admission.domain.model.ApplicantData;
import ac.dnd.server.admission.infrastructure.web.dto.request.ApplicantStatusCheckRequest;
import ac.dnd.server.admission.infrastructure.web.mapper.ApplicantWebMapper;
import ac.dnd.server.annotation.UnitTest;
import ac.dnd.server.auth.config.SecurityConfig;
import ac.dnd.server.common.support.ErrorCode;
import ac.dnd.server.common.util.JsonUtils;
import ac.dnd.server.fixture.ApplicantDataFixture;

@UnitTest
@WebMvcTest(controllers = ApplicantQueryController.class,
	excludeAutoConfiguration = SecurityAutoConfiguration.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class ApplicantQueryControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ApplicantQueryService applicantQueryService;

	@MockitoBean
	private ApplicantWebMapper applicantWebMapper;


	private static final Long DEFAULT_EVENT_ID = 1L;

	@Test
	void 지원_상태조회_성공() throws Exception {
		// Given
		final ApplicantStatusCheckRequest request = new ApplicantStatusCheckRequest(
			"홍길동",
			"test@test.com"
		);


		final String requestJson = JsonUtils.toJson(request);

		final ApplicantData result = ApplicantDataFixture.create();

		when(applicantQueryService.getApplicantStatusCheck(any()))
			.thenReturn(result);

		// When
		mockMvc.perform(post(
					"/event/{eventId}/applicant/status/check",
					DEFAULT_EVENT_ID
				)
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestJson)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.eventName").value(result.eventName()))
			.andExpect(jsonPath("$.name").value(result.name()))
			.andExpect(jsonPath("$.status").value(result.status().name()));
	}

	@ParameterizedTest(name = "입력: name={0}, email={1}")
	@MethodSource("provideInvalidApplicantStatusCheckRequests")
	void 지원_상태조회_유효성검사_실패(
		final String name,
		final String email
	) throws Exception {
		// Given
		final ApplicantStatusCheckRequest request = new ApplicantStatusCheckRequest(
			name,
			email
		);
		final String createRequestJson = JsonUtils.toJson(request);

		// When
		mockMvc.perform(post(
					"/event/{eventId}/applicant/status/check",
					DEFAULT_EVENT_ID
				)
					.contentType(MediaType.APPLICATION_JSON)
					.content(createRequestJson)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isUnprocessableEntity())
			.andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
			.andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()))
			.andExpect(jsonPath("$.status").value(ErrorCode.INVALID_INPUT_VALUE.getStatus()))
			.andExpect(jsonPath("$.errors").isArray())
			.andExpect(jsonPath(
				"$.errors",
				hasSize(greaterThanOrEqualTo(1))
			));
	}

	private static Stream<Arguments> provideInvalidApplicantStatusCheckRequests() {
		return Stream.of(
			Arguments.of(
				null,
				"test@example.com",
				"이름 null"
			),
			Arguments.of(
				"",
				"test@example.com",
				"이름 빈 문자열"
			),
			Arguments.of(
				" ",
				"test@example.com",
				"이름 공백"
			),
			Arguments.of(
				"홍길동",
				null,
				"이메일 null"
			),
			Arguments.of(
				"홍길동",
				"",
				"이메일 빈 문자열"
			),
			Arguments.of(
				"홍길동",
				" ",
				"이메일 공백"
			),
			Arguments.of(
				"홍길동",
				"invalid-email",
				"유효하지 않은 이메일 형식"
			),
			Arguments.of(
				null,
				"invalid-email",
				"이름 null, 유효하지 않은 이메일 형식"
			)
		);
	}

}
