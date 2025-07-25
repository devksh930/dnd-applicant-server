package ac.dnd.server.annotation

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.restdocs.RestDocumentationExtension

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Tag("restDocs")
@ExtendWith(MockitoExtension::class, RestDocumentationExtension::class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
annotation class RestDocsTest