package dev.wms.pwrapi.security.filters;

import dev.wms.pwrapi.entity.user.ApiUser;

import dev.wms.pwrapi.service.rateLimit.ApiUserRateLimiter;
import dev.wms.pwrapi.service.rateLimit.IpInMemoryRateLimiter;
import dev.wms.pwrapi.utils.generalExceptions.RateLimitException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class ApiKeyRateLimitFilter extends OncePerRequestFilter {

    private final ApiUserRateLimiter userRateLimiter;
    private final IpInMemoryRateLimiter ipRateLimiter;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
            handleRegisteredClient(auth);
        }
        else{
            handleUnregisteredClient(request);
        }

        filterChain.doFilter(request, response);
    }

    private void handleRegisteredClient(Authentication auth){
        ApiUser user = (ApiUser) auth.getPrincipal();

        if(!userRateLimiter.tryAccess(user)){
            throw new RateLimitException("User exceeded request limit.");
        }
    }

    private void handleUnregisteredClient(HttpServletRequest request){
        String ip = getClientIP(request);

        if(!ipRateLimiter.tryAccess(ip)){
            throw new RateLimitException("User exceeded request limit.");
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        return ip == null || ip.isEmpty() ? request.getRemoteAddr() : ip;
    }
}
