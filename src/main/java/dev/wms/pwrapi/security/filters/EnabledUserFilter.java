package dev.wms.pwrapi.security.filters;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EnabledUserFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
            UserDetails user = (UserDetails) auth.getPrincipal();

            if(user != null && (!user.isEnabled()))
                throw new DisabledException("Użytkownik został dezaktywowany przez administratora");
        }

        filterChain.doFilter(request, response);
    }
}
