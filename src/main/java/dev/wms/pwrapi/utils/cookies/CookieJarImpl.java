package dev.wms.pwrapi.utils.cookies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

@Getter
/**
 * This custom implementation of OkHttp's CookieJar provides us with quick switching cookies feature.
 *
 * For example, if user is logging in and his session cookie is changed, OkHttp will automatically swap
 * new cookie with the old one, making OAuth, or JWT authentication smoother.
 */
@Slf4j
public class CookieJarImpl implements CookieJar {

    private final Map<String, List<Cookie>> cookieStore = new HashMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, @NotNull List<Cookie> incomingCookies) {

        if (cookieStore.get(url.host()) != null) {
            log.debug("Trying to add cookies " + incomingCookies);

            for (Cookie incomingCookie : incomingCookies) {
                var cookieRemoved = cookieStore.get(url.host())
                        .removeIf(cookie -> cookie.name().equals(incomingCookie.name()));

                if (cookieRemoved) log.debug("Removed duplicate cookie " + incomingCookie.name());

                cookieStore.get(url.host()).add(incomingCookie);
            }
        } else {
            cookieStore.put(url.host(), new ArrayList<>(incomingCookies));
        }

    }

    @NotNull
    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url.host());
        log.debug("Using cookies: " + cookies);
        return cookies != null ? cookies : new ArrayList<>();
    }


}