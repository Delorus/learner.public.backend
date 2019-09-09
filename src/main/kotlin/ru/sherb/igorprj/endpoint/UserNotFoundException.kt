package ru.sherb.igorprj.endpoint

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author maksim
 * @since 09.09.2019
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class UserNotFoundException : RuntimeException {

    constructor(email: String): super("User with email [$email] not found, something broke on the server...")
}