package ru.sherb.igorprj.auth

import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.sherb.igorprj.auth.jwt.TokenProvider
import ru.sherb.igorprj.persist.entity.AppUser
import ru.sherb.igorprj.persist.repository.AppUserRepository
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank


/**
 * @author maksim
 * @since 04.09.2019
 */
@RestController
class AuthEndpoint(
        val userRepository: AppUserRepository,
        val tokenProvider: TokenProvider
) {

    @PostMapping("/v1/auth")
    fun auth(@Valid @RequestBody authRequest: AuthRequest): ResponseEntity<JWTToken> {
        //todo validate email and code
        val user = userRepository.findByEmail(authRequest.email).orElseGet {
            val user = AppUser().apply {
                this.email = authRequest.email
            }
            userRepository.save(user)
        }

        val authentication = UsernamePasswordAuthenticationToken(user.email, "")
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = tokenProvider.createToken(authentication)
        return ResponseEntity.ok(JWTToken(jwt))
    }
}

/**
 * Object to return as body in JWT Authentication.
 */
data class JWTToken(val token: String)

data class AuthRequest(
        @Email
        @NotBlank
        val email: String,
        @NotBlank
        val code: String)