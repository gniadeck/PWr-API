package dev.wms.pwrapi.utils.cookies;

import lombok.SneakyThrows;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CookieJarImplTest {

    private CookieJarImpl underTest;

    @BeforeEach
    void reset() {
        underTest = new CookieJarImpl();
    }

    @Test
    void shouldSaveFromResponseIfNoCookiesPresent() {
        HttpUrl url = HttpUrl.parse("http://test.wmsdev.pl");
        List<Cookie> cookies = generateCookies(5);

        underTest.saveFromResponse(url, cookies);

        assertEquals(5, getCurrentCookieState().get(url.host()).size());
    }

    @Test
    void shouldReplaceCookiesForNewOnes() {
        HttpUrl url = HttpUrl.parse("http://test.wmsdev.pl");
        List<Cookie> cookies = generateCookies(5);
        List<Cookie> cookiesToReplace = cloneCookiesAndSetValueTo(cookies, "newValue");

        underTest.saveFromResponse(url, cookies);
        underTest.saveFromResponse(url, cookiesToReplace);

        var cookieState = getCurrentCookieState();
        assertEquals(5, cookieState.get(url.host()).size());
        cookieState.get(url.host()).forEach(cookie -> assertEquals("newValue", cookie.value()));

    }

    @Test
    void shouldNotAddNewCookiesWhenOldPresent() {
        HttpUrl url = HttpUrl.parse("http://test.wmsdev.pl");
        List<Cookie> cookies = generateCookies(5);

        underTest.saveFromResponse(url, cookies);
        underTest.saveFromResponse(url, cookies);

        assertEquals(5, getCurrentCookieState().get(url.host()).size());
    }

    @Test
    void shouldLoadForRequestForCertainUrl() {
        HttpUrl url = HttpUrl.parse("http://test.wmsdev.pl");
        List<Cookie> cookies = generateCookies(5);

        underTest.saveFromResponse(url, cookies);
        var loadedCookies = underTest.loadForRequest(url);

        cookies.forEach(cookie -> assertTrue(loadedCookies.contains(cookie)));
    }

    @Test
    void shouldReturnEmptyListWhenLoadingForRequestOnNull() {
        HttpUrl url = HttpUrl.parse("http://test.wmsdev.pl");

        var loadedCookies = underTest.loadForRequest(url);

        assertEquals(0, loadedCookies.size());
    }

    private List<Cookie> generateCookies(int size) {
        return IntStream.range(0, size)
                .mapToObj(integer -> mockedCookie())
                .toList();
    }

    private void setCookieValue(Cookie cookie, String newValue) {
        setCookieField(cookie, "value", newValue);
    }

    private void setCookieName(Cookie cookie, String newName) {
        setCookieField(cookie, "name", newName);
    }

    private List<Cookie> cloneCookiesAndSetValueTo(List<Cookie> cookies, String newValue) {
        return cookies.stream()
                .map(cookie -> {
                    var generatedCookie = mockedCookie();
                    setCookieName(generatedCookie, cookie.name());
                    setCookieValue(generatedCookie, newValue);
                    return generatedCookie;
                })
                .toList();
    }

    private Cookie mockedCookie() {
        try {
            return (Cookie) Cookie.class.getConstructors()[0].newInstance(RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(20), 2000000000000L, RandomStringUtils.randomAlphanumeric(15),
                    RandomStringUtils.randomAlphanumeric(20), false, false, true, false, null);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private void setCookieField(Cookie cookie, String fieldName, String value) {
        Field field = cookie.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(cookie, value);
    }

    @SneakyThrows
    private Map<String, List<Cookie>> getCurrentCookieState() {
        var cookieStoreField = underTest.getClass().getDeclaredField("cookieStore");
        cookieStoreField.setAccessible(true);
        return (Map<String, List<Cookie>>) cookieStoreField.get(underTest);
    }

}
