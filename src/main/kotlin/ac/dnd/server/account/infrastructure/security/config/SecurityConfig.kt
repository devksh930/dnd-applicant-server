package ac.dnd.server.account.infrastructure.security.config

import ac.dnd.server.account.infrastructure.persistence.repository.AccountJpaRepository
import ac.dnd.server.account.infrastructure.persistence.repository.RefreshTokenRepository
import ac.dnd.server.account.infrastructure.security.authentication.filter.JsonUsernamePasswordAuthenticationFilter
import ac.dnd.server.account.infrastructure.security.authentication.handler.RestAuthFailureHandler
import ac.dnd.server.account.infrastructure.security.authentication.handler.RestAuthSuccessHandler
import ac.dnd.server.account.infrastructure.security.jwt.JwtAuthenticationFilter
import ac.dnd.server.account.infrastructure.security.jwt.JwtTokenProvider
import ac.dnd.server.shared.config.properties.JwtProperties
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
class SecurityConfig(
    private val corsConfigurationSource: CorsConfigurationSource,
    private val objectMapper: ObjectMapper,
    private val restAuthSuccessHandler: RestAuthSuccessHandler,
    private val restAuthFailureHandler: RestAuthFailureHandler,
    private val environment: Environment,
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

        private val DEV_ONLY_URLS =
            arrayOf(
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/swagger/**",
                "/v3/api-docs/**",
                "/webjars/**",
                "/docs/**"
            )
    }

    private fun isDevProfile(): Boolean {
        return environment.activeProfiles.contains("dev")
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jsonUsernamePasswordAuthenticationFilter: JsonUsernamePasswordAuthenticationFilter,
        jwtAuthenticationFilter: JwtAuthenticationFilter
    ): SecurityFilterChain {
        http.csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource) }
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
                if (isDevProfile()) {
                    auth.requestMatchers(*DEV_ONLY_URLS).permitAll()
                }
                auth.anyRequest().authenticated()
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
        jwtTokenProvider: JwtTokenProvider,
        refreshTokenRepository: RefreshTokenRepository,
        accountJpaRepository: AccountJpaRepository,
        jwtProperties: JwtProperties
    ): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(jwtTokenProvider, refreshTokenRepository, accountJpaRepository, jwtProperties)
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
}
