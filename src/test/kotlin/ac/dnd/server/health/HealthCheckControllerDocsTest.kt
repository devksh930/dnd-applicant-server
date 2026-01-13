package ac.dnd.server.health

import ac.dnd.server.annotation.RestDocsTest
import ac.dnd.server.documenation.DocumentUtils
import ac.dnd.server.documenation.MockMvcFactory
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@RestDocsTest
class HealthCheckControllerDocsTest {

    @InjectMocks
    private lateinit var controller: HealthCheckController

    companion object {
        private const val HOST_API = "api.dnd.ac"
    }

    @Test
    fun `헬스 체크 API 문서화`(
        contextProvider: RestDocumentationContextProvider
    ) {
        // when & then
        MockMvcFactory.getRestDocsMockMvc(
            contextProvider,
            HOST_API,
            controller
        ).perform(
            RestDocumentationRequestBuilders.get("/health")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                DocumentUtils.document(
                    "get-health-check",
                    "시스템",
                    "헬스 체크",
                    "서버의 상태를 확인합니다."
                )
            )
    }
}
