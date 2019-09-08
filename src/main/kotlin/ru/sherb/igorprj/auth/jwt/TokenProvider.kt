package ru.sherb.igorprj.auth.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.security.Key
import java.time.Duration
import java.time.Instant
import java.util.*

@Component
class TokenProvider {

    private var key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS512)

    @Value("\${learner.jwt.expiredTime}")
    private lateinit var expiredTime: Duration

    @Value("\${learner.security.admin_email}")
    private lateinit var superEmail: String

    fun createSuperToken(): String {
        return Jwts.builder()
                .setSubject(superEmail)
                .claim(AUTHORITIES_KEY, "ROLE_ADMIN")
                .signWith(key, SignatureAlgorithm.HS512)
                .compact()
    }

    fun createToken(authentication: Authentication): String {
        if (authentication.name == superEmail) {
            return createSuperToken()
        }

        val authorities = authentication.authorities.joinToString { it.authority }

        val validity = Instant.now().plus(expiredTime)

        return Jwts.builder()
                .setSubject(authentication.name)
                .claim(AUTHORITIES_KEY, arrayOf(authorities, "ROLE_USER").joinToString())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(Date.from(validity))
                .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .body

        val authorities = claims[AUTHORITIES_KEY]
                .toString()
                .split(",")
                .takeWhile { it.isNotBlank() }
                .map { SimpleGrantedAuthority(it) }

        val principal = User(claims.subject, claims.subject, authorities)

        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    fun validateToken(authToken: String) = try {
        Jwts.parser().setSigningKey(key).parseClaimsJws(authToken)
        true
    } catch (e: SecurityException) { false } catch (e: MalformedJwtException) { false } catch (e: SignatureException) { false }

    companion object {
        private const val AUTHORITIES_KEY = "auth"
    }
}
