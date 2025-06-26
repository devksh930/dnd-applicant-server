package ac.dnd.server.admission.auth.config

import ac.dnd.server.admission.infrastructure.persistence.crypto.HmacBlindIndexCreator
import ac.dnd.server.common.propeties.CorsProperties
import ac.dnd.server.common.propeties.EncryptionProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(
    private val encryptionProperties: EncryptionProperties,
    private val corsProperties: CorsProperties
) {


    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }

        http.headers { headers ->
            headers.xssProtection { xss ->
                xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
            }.contentSecurityPolicy { cps ->
                cps.policyDirectives("script-src 'self' .....")
            }
        }

        http.authorizeHttpRequests { auth ->
            auth.requestMatchers(
                "/event/{eventId}/applicant/status/check",
                "/events/current",
                "/health",
            ).permitAll()
                .anyRequest().authenticated()
        }

        return http.build()
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
        ) // 모든 경로에 대해 적용
        return source
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
