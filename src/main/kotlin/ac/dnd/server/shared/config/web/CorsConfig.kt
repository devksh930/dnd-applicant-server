package ac.dnd.server.shared.config.web

import ac.dnd.server.shared.config.properties.CorsProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig(
    private val corsProperties: CorsProperties,
) {

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration().apply {
            allowedOriginPatterns = corsProperties.allowedOriginsPatterns
            allowedHeaders = corsProperties.allowedHeaders
            exposedHeaders = corsProperties.exposedHeaders
            allowCredentials = corsProperties.allowCredentials
            maxAge = corsProperties.maxAge
            allowedMethods = corsProperties.allowedMethods
        }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", corsConfiguration)
        }
    }
}
