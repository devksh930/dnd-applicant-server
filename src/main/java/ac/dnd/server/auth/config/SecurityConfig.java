package ac.dnd.server.auth.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
	private static final List<String> ALLOW_ORIGIN_PATTERN = List.of(
		"http://localhost:*");

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
		corsConfiguration.setAllowedOriginPatterns(ALLOW_ORIGIN_PATTERN);
		corsConfiguration.setAllowedMethods(List.of(
			"OPTIONS",
			"GET",
			"POST",
			"PATCH",
			"PUT",
			"DELETE"
		));
		corsConfiguration.setAllowedHeaders(List.of(
			"Authorization",
			"Content-Type"
		));
		corsConfiguration.setExposedHeaders(List.of(
			"Authorization",
			"Location"
		));
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration(
			"/**",
			corsConfiguration
		); // 모든 경로에 대해 적용
		return source;
	}
}
