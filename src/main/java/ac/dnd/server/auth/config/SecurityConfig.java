package ac.dnd.server.auth.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import ac.dnd.server.admission.infrastructure.persistence.crypto.HmacBlindIndexCreator;
import ac.dnd.server.common.propeties.CorsProperties;
import ac.dnd.server.common.propeties.EncryptionProperties;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final EncryptionProperties encryptionProperties;
	private final CorsProperties corsProperties;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.cors(s -> s.configurationSource(corsConfigurationSource()))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable);

		http.headers(headers -> headers.xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
			.contentSecurityPolicy(cps -> cps.policyDirectives("script-src 'self' .....")));

		return http.build();
	}

	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedOriginPatterns(corsProperties.getAllowedOriginsPatterns());
		corsConfiguration.setAllowedHeaders(corsProperties.getAllowedHeaders());
		corsConfiguration.setExposedHeaders(corsProperties.getExposedHeaders());
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setMaxAge(3600L);
		corsConfiguration.setAllowedMethods(List.of(
			HttpMethod.HEAD.name(),
			HttpMethod.OPTIONS.name(),
			HttpMethod.GET.name(),
			HttpMethod.POST.name(),
			HttpMethod.PATCH.name(),
			HttpMethod.PUT.name(),
			HttpMethod.DELETE.name()
		));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration(
			"/**",
			corsConfiguration
		); // 모든 경로에 대해 적용
		return source;
	}

	@Bean
	public TextEncryptor textEncryptor() {

		return Encryptors.text(
			encryptionProperties.getAes().getPassword(),
			encryptionProperties.getAes().getSalt()
		);
	}

	@Bean
	public HmacBlindIndexCreator hmacBlindIndexCreator() {
		return new HmacBlindIndexCreator(encryptionProperties);
	}
}
