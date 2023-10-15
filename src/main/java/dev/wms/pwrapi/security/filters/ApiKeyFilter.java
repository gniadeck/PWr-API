package dev.wms.pwrapi.security.filters;

import dev.wms.pwrapi.entity.user.ApiUser;
import dev.wms.pwrapi.service.metrics.ApiMetricsService;
import dev.wms.pwrapi.service.user.ApiUserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private final String API_KEY_HEADER_NAME;
    private final ApiUserService userService;
    private final ApiMetricsService metricsService;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain) throws ServletException, IOException {

        String apiKey = request.getHeader(API_KEY_HEADER_NAME);

        if(apiKey != null && !apiKey.isEmpty())
        {
            Optional<ApiUser> user = userService.getUserByApiKey(apiKey);
            if(user.isPresent()) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(user.get(), apiKey, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                metricsService.onAuthenticationCorrect();
            } else {
                metricsService.onAuthenticationFailed();
            }

        }
        filterChain.doFilter(request, response);
    }
}
