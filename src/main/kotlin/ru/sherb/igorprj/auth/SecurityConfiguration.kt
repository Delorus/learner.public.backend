package ru.sherb.igorprj.auth

import org.springframework.beans.factory.BeanInitializationException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.DefaultSecurityFilterChain
import ru.sherb.igorprj.auth.jwt.JWTConfigurer
import ru.sherb.igorprj.auth.jwt.TokenProvider
import javax.annotation.PostConstruct

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
        val authenticationManagerBuilder: AuthenticationManagerBuilder,
        val userDetailsService: UserDetailsService,
        val tokenProvider: TokenProvider
) : WebSecurityConfigurerAdapter() {

    @PostConstruct
    fun init() {
        try {
            authenticationManagerBuilder.userDetailsService(userDetailsService)
        } catch (e: Exception) {
            throw BeanInitializationException("Security configuration failed", e)
        }

    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    public override fun configure(http: HttpSecurity) {
        //@formatter:off
        http
            .csrf()
                .disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().authorizeRequests()
                .antMatchers("/v1/auth").permitAll()
                .antMatchers("/v1/sendCode").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .anyRequest().authenticated()
            .and()
                .apply<SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>>(securityConfigurerAdapter())
        //@formatter:on
    }

    private fun securityConfigurerAdapter(): JWTConfigurer {
        return JWTConfigurer(tokenProvider)
    }
}
