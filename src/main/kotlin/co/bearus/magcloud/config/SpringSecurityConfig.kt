package co.bearus.magcloud.config

import co.bearus.magcloud.config.filter.ApplicationFilter
import co.bearus.magcloud.config.filter.CustomJWTFilter
import co.bearus.magcloud.config.handler.AuthEntryPoint
import co.bearus.magcloud.config.handler.WebAccessDeniedHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@Configuration
class SpringSecurityConfig(
    private val applicationFilter: ApplicationFilter,
    private val authEntryPoint: AuthEntryPoint,
    private val webAccessDeniedHandler: WebAccessDeniedHandler,
    private val jwtFilter: CustomJWTFilter,
) {

    @Bean
    fun config(http: HttpSecurity): SecurityFilterChain? {
        return http
            .headers {
                it.frameOptions { frameOption ->
                    frameOption.sameOrigin()
                }
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("/v1/auth/**").permitAll()
                    .requestMatchers("/health-check").permitAll()
                    .anyRequest().authenticated()
            }
            .csrf {
                it.disable()
            }
            .addFilterBefore(applicationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it.authenticationEntryPoint(authEntryPoint)
                    .accessDeniedHandler(webAccessDeniedHandler)
            }
            .build()
    }
}
