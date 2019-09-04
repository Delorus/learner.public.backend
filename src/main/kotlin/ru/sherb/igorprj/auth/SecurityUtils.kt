package ru.sherb.igorprj.auth


import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

/**
 * Utility class for Spring Security.
 */
object SecurityUtils {

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user
     */
    val currentUserLogin: String?
        get() {
            val securityContext = SecurityContextHolder.getContext()
            return securityContext?.authentication
                    .let { authentication ->
                        if (authentication?.principal is UserDetails) {
                            val springSecurityUser = authentication.principal as UserDetails
                            return@let springSecurityUser.username
                        } else if (authentication?.principal is String) {
                            return@let authentication.principal as String
                        }
                        null
                    }
        }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user
     */
    val currentUserJWT: String?
        get() {
            val securityContext = SecurityContextHolder.getContext()
            return securityContext?.authentication
                    .takeIf { authentication -> authentication?.credentials is String }
                    .let { authentication -> authentication?.credentials as String }
        }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    val isAuthenticated: Boolean
        get() {
            val securityContext = SecurityContextHolder.getContext()
            return securityContext.authentication
                    .let { authentication ->
                        authentication.authorities.stream()
                                .noneMatch { grantedAuthority -> grantedAuthority.authority == AuthoritiesConstants.ANONYMOUS }
                    }
        }

    /**
     * If the current user has a specific authority (security role).
     *
     *
     * The name of this method comes from the isUserInRole() method in the Servlet API
     *
     * @param authority the authority to check
     * @return true if the current user has the authority, false otherwise
     */
    fun isCurrentUserInRole(authority: String): Boolean {
        val securityContext = SecurityContextHolder.getContext()
        return securityContext.authentication
                .let { authentication ->
                    authentication.authorities.stream()
                            .anyMatch { grantedAuthority -> grantedAuthority.authority == authority }
                }
    }
}
