package ac.dnd.server.documenation

import ac.dnd.server.shared.web.GlobalExceptionHandler
import ac.dnd.server.common.util.JsonUtils
import ac.dnd.server.common.util.LocalDateTimeUtils
import ac.dnd.server.common.util.LocalDateUtils
import ac.dnd.server.common.util.LocalTimeUtils
import org.springframework.core.convert.converter.Converter
import org.springframework.format.support.DefaultFormattingConversionService
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.filter.CharacterEncodingFilter
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object MockMvcFactory {
    fun getMockMvc(vararg controllers: Any): MockMvc {
        return getMockMvcBuilder(*controllers).build()
    }

    fun getRestDocsMockMvc(
        restDocumentationContextProvider: RestDocumentationContextProvider,
        host: String,
        vararg controllers: Any
    ): MockMvc {
        val documentationConfigurer = documentationConfiguration(restDocumentationContextProvider)
            .apply {
                uris().withScheme("https").withHost(host).withPort(443)
            }

        return getMockMvcBuilder(*controllers)
            .apply<StandaloneMockMvcBuilder>(documentationConfigurer)
            .build()
    }

    private fun getMockMvcBuilder(vararg controllers: Any): StandaloneMockMvcBuilder {
        val conversionService = DefaultFormattingConversionService().apply {
            addConverter(LocalDateTimeConverter())
            addConverter(LocalDateConverter())
            addConverter(LocalTimeConverter())
        }

        return MockMvcBuilders.standaloneSetup(*controllers)
            .setControllerAdvice(GlobalExceptionHandler())
            .setConversionService(conversionService)
            .setMessageConverters(MappingJackson2HttpMessageConverter(JsonUtils.getMapper()))
            .addFilter(CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
    }

    class LocalDateTimeConverter : Converter<String, LocalDateTime> {
        override fun convert(source: String): LocalDateTime? {
            return LocalDateTimeUtils.toLocalDateTime(source, "yyyy-MM-dd HH:mm:ss")
        }
    }

    class LocalDateConverter : Converter<String, LocalDate> {
        override fun convert(source: String): LocalDate? {
            return LocalDateUtils.toLocalDate(source)
        }
    }

    class LocalTimeConverter : Converter<String, LocalTime> {
        override fun convert(source: String): LocalTime? {
            return LocalTimeUtils.toLocalTime(source)
        }
    }
}
