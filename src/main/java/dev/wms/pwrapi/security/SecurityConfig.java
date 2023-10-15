package dev.wms.pwrapi.security;

import dev.wms.pwrapi.security.filters.ApiKeyFilter;
import dev.wms.pwrapi.security.filters.EnabledUserFilter;
import dev.wms.pwrapi.security.filters.ExceptionHandlerFilter;
import dev.wms.pwrapi.security.filters.ApiKeyRateLimitFilter;
import dev.wms.pwrapi.service.rateLimit.ApiUserRateLimiter;
import dev.wms.pwrapi.service.rateLimit.IpInMemoryRateLimiter;
import dev.wms.pwrapi.service.metrics.ApiMetricsService;
import dev.wms.pwrapi.service.user.ApiUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${security.authenticate}")
    private boolean SECURITY_ENABLED;

    @Value("${api-key.header-name}")
    private String API_KEY_HEADER_NAME;

    private final ApiUserService userService;
    private final ApiMetricsService metricsService;

    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final PasswordEncoder passwordEncoder;

    private final IpInMemoryRateLimiter rateLimiter;
    private final ApiUserRateLimiter userRateLimiter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return SECURITY_ENABLED ? securityOn(http) : securityOff(http);
    }

    private SecurityFilterChain securityOff(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .build();
    }

    private SecurityFilterChain securityOn(HttpSecurity http) throws Exception {

        return http
                .csrf().disable()
                .authorizeHttpRequests()

                .regexMatchers(".*/api/developers.*", ".*/swagger-ui/.*", ".*/actuator/.*", ".*api-docs.*")
                    .permitAll()
                .anyRequest()
                    .authenticated()
                    .and()

                .addFilterBefore(new ApiKeyFilter(
                                API_KEY_HEADER_NAME, userService, metricsService),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterAfter(
                        new EnabledUserFilter(),
                        ApiKeyFilter.class
                )
                .addFilterAfter(
                        new ApiKeyRateLimitFilter(userRateLimiter, rateLimiter),
                        EnabledUserFilter.class
                )
                .addFilterBefore(exceptionHandlerFilter, ApiKeyFilter.class)

                .authenticationProvider(authenticationProvider())

                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()

                .exceptionHandling()
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                    .and()

                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }
}
