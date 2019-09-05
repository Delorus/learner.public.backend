package ru.sherb.igorprj.auth

import com.warrenstrange.googleauth.GoogleAuthenticatorException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author maksim
 * @since 05.09.2019
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
class AuthenticationException : RuntimeException {

    constructor(email: String, cause: GoogleAuthenticatorException) : super("incorrect email or code for user [$email]", cause)
}