package ru.sherb.igorprj.auth


import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.sherb.igorprj.persist.entity.AppUser
import ru.sherb.igorprj.persist.repository.AppUserRepository

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
class DomainUserDetailsService(private val userRepository: AppUserRepository) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(email: String): UserDetails = userRepository.findByEmail(email)
            .map(this::createSpringSecurityUser)
            .orElseThrow { UsernameNotFoundException("User with email $email was not found in the database") }

    private fun createSpringSecurityUser(user: AppUser): org.springframework.security.core.userdetails.User {

        return org.springframework.security.core.userdetails.User(user.email, "", null)
    }
}
