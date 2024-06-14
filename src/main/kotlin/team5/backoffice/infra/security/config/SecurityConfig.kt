package team5.backoffice.infra.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import team5.backoffice.infra.security.jwt.JwtAuthenticationFilter
import team5.backoffice.infra.swagger.SwaggerConfig

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {
    @Bean
    fun filterChain(http: HttpSecurity, swaggerConfig: SwaggerConfig): SecurityFilterChain {
        return http
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/auth/tutor/login",
                    "/auth/tutor/signup",
                    "/auth/student/login",
                    "/auth/student/signup",
                    "/oauth/naver",
                    "/oauth/naver/callback",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/"
                ).permitAll().anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}