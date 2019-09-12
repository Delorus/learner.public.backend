package ru.sherb.igorprj.auth

import com.warrenstrange.googleauth.GoogleAuthenticator
import com.warrenstrange.googleauth.GoogleAuthenticatorException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.sherb.igorprj.auth.jwt.TokenProvider
import ru.sherb.igorprj.auth.otp.OtpStorage
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
@RequestMapping("/v1")
class AuthEndpoint(
        val userRepository: AppUserRepository,
        val tokenProvider: TokenProvider,
        val otpStorage: OtpStorage
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/auth")
    fun auth(@Valid @RequestBody authRequest: AuthRequest): ResponseEntity<AuthResponse> {
        val secret = otpStorage[authRequest.email]
        val gAuth = GoogleAuthenticator()
        try {
            if (secret == null || !gAuth.authorize(secret, authRequest.code.toInt())) {
                throw AuthenticationException(authRequest.email)
            }
        } catch (e: GoogleAuthenticatorException) {
            throw AuthenticationException(authRequest.email, e)
        }
        otpStorage.remove(authRequest.email)

        val user = userRepository.findByEmail(authRequest.email).orElseGet {
            val user = AppUser().apply {
                this.email = authRequest.email
            }
            userRepository.save(user)
        }

        val authentication = UsernamePasswordAuthenticationToken(user.email, "")
        SecurityContextHolder.getContext().authentication = authentication

        val jwt = tokenProvider.createToken(authentication)
        return ResponseEntity.ok(AuthResponse(jwt, user.id))
    }

    @PostMapping("/sendCode")
    fun sendCode(@Valid @Email @NotBlank @RequestBody email: String): ResponseEntity<DebugCodeResponse> {
        val gAuth = GoogleAuthenticator()
        val key = gAuth.createCredentials()
        otpStorage[email] = key.key
        val code = gAuth.getTotpPassword(key.key)
        log.warn("AUTHORISATION CODE: $code")
        return ResponseEntity.ok(DebugCodeResponse(code))
    }
}

data class DebugCodeResponse(val code: Int)

/**
 * Object to return as body in JWT Authentication.
 */
data class AuthResponse(val token: String, val userId: Int)

data class AuthRequest(
        @Email
        @NotBlank
        val email: String,
        @NotBlank
        val code: String)