package dev.wms.pwrapi.utils.config;

import com.mysql.cj.util.StringUtils;
import dev.wms.pwrapi.entity.user.ApiUser;
import io.sentry.Scope;
import io.sentry.Sentry;
import io.sentry.UserFeedback;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;


@Component
public class SentryReporter implements ExceptionHandler {

    @Override
    public String report(Throwable throwable) {
        Sentry.configureScope(this::setUser);
        return Sentry.captureException(throwable).toString();
    }

    public void captureFeedback(String comment, String email, String name, String errorId) {
        UserFeedback userFeedback = getUserFeedbackErrorId(errorId);

        userFeedback.setComments(comment);
        if (!StringUtils.isNullOrEmpty(email)) userFeedback.setEmail(email);
        if (!StringUtils.isNullOrEmpty(name)) userFeedback.setName(name);
        Sentry.captureUserFeedback(userFeedback);
    }

    @NotNull
    private UserFeedback getUserFeedbackErrorId(String errorId) {
        if (StringUtils.isNullOrEmpty(errorId)) {
            SentryId sentryId = Sentry.captureMessage("User feedback submitted");
            return new UserFeedback(sentryId);
        }
        return new UserFeedback(new SentryId(errorId));
    }

    public void captureMessage(String message) {
        Sentry.configureScope(this::setUser);
        Sentry.captureMessage(message);
    }

    private void setUser(Scope scope) {
        User user = new User();
        user.setIpAddress(getIp());
        user.setUsername(getUsername());
        addSecurityDetails(user);
        scope.setUser(user);
    }

    private void addSecurityDetails(User user) {
        Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .map(auth -> ((ApiUser) auth))
                .ifPresent(apiUser -> {
                    user.setEmail(apiUser.getEmail());
                    user.setId(apiUser.getId().toString());
                });
    }

    private String getUsername() {
        var username = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getParameter("login");
        return username == null ? "anonymous" : username;
    }

    private String getIp() {
        var ip = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getHeader("X-Forwarded-For");
        if (ip == null || ip.equals("0:0:0:0:0:0:0:1")) {
            return "127.0.0.1";
        }
        return ip;
    }
}
