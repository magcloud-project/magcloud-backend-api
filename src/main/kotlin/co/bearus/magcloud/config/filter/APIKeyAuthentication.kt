package co.bearus.magcloud.config.filter

import org.springframework.security.core.Authentication

class APIKeyAuthentication(
    private val userId: String,
    private val token: String,
) : Authentication {
    private var authenticated = true
    override fun getName() = this.userId

    override fun getAuthorities() = null

    override fun getCredentials() = this.token

    override fun getDetails() = null

    override fun getPrincipal() = ""

    override fun isAuthenticated() = this.authenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.authenticated = isAuthenticated
    }
}
