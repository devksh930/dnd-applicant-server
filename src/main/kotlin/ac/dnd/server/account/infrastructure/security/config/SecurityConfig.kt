package ac.dnd.server.account.infrastructure.security.config

import ac.dnd.server.account.infrastructure.security.authentication.filter.JsonUsernamePasswordAuthenticationFilter
import ac.dnd.server.account.infrastructure.security.authentication.handler.RestAuthFailureHandler
import ac.dnd.server.account.infrastructure.security.authentication.handler.RestAuthSuccessHandler
import ac.dnd.server.account.infrastructure.security.jwt.JwtAuthenticationFilter
import ac.dnd.server.account.infrastructure.security.jwt.JwtTokenProvider
import ac.dnd.server.admission.infrastructure.persistence.crypto.HmacBlindIndexCreator
import ac.dnd.server.shared.config.properties.CorsProperties
import ac.dnd.server.shared.config.properties.EncryptionProperties
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(
    private val encryptionProperties: EncryptionProperties,
    private val corsProperties: CorsProperties,
    private val objectMapper: ObjectMapper,
    private val restAuthSuccessHandler: RestAuthSuccessHandler,
    private val restAuthFailureHandler: RestAuthFailureHandler,
) {
    companion object {
        private val PERMIT_ALL_URLS =
            arrayOf(
                "/event/{eventId}/applicant/status/check",
                "/events/current",
                "/health",
                "/google/form",
                "/auth"
            )
    }


    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jsonUsernamePasswordAuthenticationFilter: JsonUsernamePasswordAuthenticationFilter,
        jwtAuthenticationFilter: JwtAuthenticationFilter
    ): SecurityFilterChain {
        http.csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .headers { headers ->
                headers.xssProtection { xss ->
                    xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                }.contentSecurityPolicy { cps ->
                    cps.policyDirectives("script-src 'self' .....")
                }
            }
            .formLogin { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .httpBasic { it.disable() }

            .authorizeHttpRequests { auth ->
                auth.requestMatchers(*PERMIT_ALL_URLS).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )
            .addFilterAt(
                jsonUsernamePasswordAuthenticationFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }

    @Bean
    fun jwtAuthenticationFilter(
        jwtTokenProvider: JwtTokenProvider
    ): JwtAuthenticationFilter {
        PersistenceExceptionTranslationPostProcessor()
        return JwtAuthenticationFilter(jwtTokenProvider)
    }

    fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.allowedOriginPatterns = corsProperties.allowedOriginsPatterns
        corsConfiguration.allowedHeaders = corsProperties.allowedHeaders
        corsConfiguration.exposedHeaders = corsProperties.exposedHeaders
        corsConfiguration.allowCredentials = corsProperties.allowCredentials
        corsConfiguration.maxAge = corsProperties.maxAge
        corsConfiguration.allowedMethods = corsProperties.allowedMethods

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration(
            "/**",
            corsConfiguration
        )
        return source
    }

    @Bean
    fun jsonUsernamePasswordAuthenticationFilter(
        authenticationManager: AuthenticationManager
    ): JsonUsernamePasswordAuthenticationFilter {
        val filter = JsonUsernamePasswordAuthenticationFilter(objectMapper)
        filter.setAuthenticationManager(authenticationManager)
        filter.setAuthenticationSuccessHandler(restAuthSuccessHandler)
        filter.setAuthenticationFailureHandler(restAuthFailureHandler)
        return filter
    }

    @Bean
    fun authenticationManager(
        authenticationConfiguration: AuthenticationConfiguration
    ): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun textEncryptor(): TextEncryptor {
        return Encryptors.text(
            encryptionProperties.aes.password,
            encryptionProperties.aes.salt
        )
    }

    @Bean
    fun hmacBlindIndexCreator(): HmacBlindIndexCreator {
        return HmacBlindIndexCreator(encryptionProperties)
    }
}