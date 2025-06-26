package ac.dnd.server.annotation

import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator
import org.junit.jupiter.api.Tag

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
@Tag("UnitTest")
annotation class UnitTest