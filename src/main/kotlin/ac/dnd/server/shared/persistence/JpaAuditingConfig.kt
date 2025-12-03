package ac.dnd.server.shared.persistence

import ac.dnd.server.account.infrastructure.security.authentication.userdetails.AccountDetails
import ac.dnd.server.shared.dto.LoginInfo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import java.util.Optional
import java.util.UUID

@Configuration
@EnableJpaAuditing
class JpaAuditingConfig {

    @Bean
    fun auditorAware(): AuditorAware<UUID> = SecurityAuditorAware()
}

class SecurityAuditorAware : AuditorAware<UUID> {
    override fun getCurrentAuditor(): Optional<UUID> {
        val authentication = SecurityContextHolder.getContext().authentication ?: return Optional.empty()
        if (!authentication.isAuthenticated) return Optional.empty()

        val principal = authentication.principal
        return when (principal) {
            is AccountDetails -> Optional.of(principal.userKey)
            is LoginInfo -> Optional.of(principal.userId)
            is UUID -> Optional.of(principal)
            else -> Optional.empty()
        }
    }
}
