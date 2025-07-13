package ac.dnd.server.annotation

import ac.dnd.server.TestcontainersConfiguration
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Tag("IntegrationTest")
@ExtendWith(SpringExtension::class)
@SpringBootTest
@Import(TestcontainersConfiguration::class)
annotation class IntegrationTest