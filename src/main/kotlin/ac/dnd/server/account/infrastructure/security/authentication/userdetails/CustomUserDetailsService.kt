package ac.dnd.server.account.infrastructure.security.authentication.userdetails

import ac.dnd.server.account.infrastructure.persistence.repository.AccountJpaRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomUserDetailsService(
    private val accountJpaRepository: AccountJpaRepository
) : UserDetailsService {

    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String): UserDetails {
        val account = accountJpaRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("User not found with email: $username")

        return AccountDetails(
            userKey = account.userKey.value,
            email = account.email,
            password = account.password,
            authorities = listOf(SimpleGrantedAuthority("ROLE_${account.role.name}"))
        )
    }

}
