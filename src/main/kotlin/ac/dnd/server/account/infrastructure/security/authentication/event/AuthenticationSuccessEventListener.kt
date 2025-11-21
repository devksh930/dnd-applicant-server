package ac.dnd.server.account.infrastructure.security.authentication.event

import ac.dnd.server.account.infrastructure.persistence.entity.UserKey
import ac.dnd.server.account.infrastructure.persistence.repository.AccountJpaRepository
import ac.dnd.server.account.infrastructure.security.authentication.userdetails.AccountDetails
import org.springframework.context.event.EventListener
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class AuthenticationSuccessEventListener(
    private val accountJpaRepository: AccountJpaRepository
) {

    @EventListener
    @Transactional
    fun handleAuthenticationSuccess(event: InteractiveAuthenticationSuccessEvent) {
        val accountDetails = event.authentication.principal as? AccountDetails ?: return

        accountJpaRepository.updateLastLoginDateByUserKey(
            UserKey(accountDetails.userKey),
            LocalDateTime.now()
        )
    }
}
