package co.bearus.magcloud.config

import co.bearus.magcloud.config.filter.ApplicationFilter
import co.bearus.magcloud.config.filter.JWTFilter
import co.bearus.magcloud.config.handler.AuthEntryPoint
import co.bearus.magcloud.config.handler.WebAccessDeniedHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@EnableWebSecurity
@Configuration
class SpringSecurityConfig(
    private val applicationFilter: ApplicationFilter,
    private val jwtFilter: JWTFilter,
    private val authEntryPoint: AuthEntryPoint,
    private val webAccessDeniedHandler: WebAccessDeniedHandler,
) {
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun config(http: HttpSecurity): SecurityFilterChain? {
        return http
            .exceptionHandling().authenticationEntryPoint(authEntryPoint).accessDeniedHandler(webAccessDeniedHandler)
            .and()
            .headers().frameOptions().sameOrigin()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .requestMatchers("/v1/auth/**").permitAll()
            .requestMatchers("/health-check").permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf().disable()
            .addFilterBefore(applicationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}
