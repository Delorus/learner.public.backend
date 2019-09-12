package ru.sherb.igorprj.auth.otp

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

/**
 * @author maksim
 * @since 05.09.2019
 */
@Component
class OtpStorage {

    private val emailToCode: MutableMap<String, String> = ConcurrentHashMap() //todo set expired date

    operator fun get(email: String) = emailToCode[email]

    operator fun set(email: String, code: String) {
        emailToCode[email.trim()] = code
    }

    fun remove(email: String) = emailToCode.remove(email)
}