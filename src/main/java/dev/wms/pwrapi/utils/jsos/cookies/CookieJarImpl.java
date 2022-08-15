package dev.wms.pwrapi.utils.jsos.cookies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

/**
 * This is a custom implementation of OkHttp's CookieJar class. It swipes cookies if duplicate is detected
 * in order to always have authenticated and most recent cookies given by the server.
 *
 * Mostly used in accessing services that require OAuth protocol usage. It allows us to quickly swipe
 * session cookies and some other tokens.
 */
@Getter
public class CookieJarImpl implements CookieJar{

    private final HashMap<String, ArrayList<Cookie>> cookieStore = new HashMap<>();
    
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                

                if(cookieStore.get(url.host()) != null && cookies != null){
                    System.out.println("Trying to add cookies " + cookies);
                    for(Cookie c : cookies){
                        
                        //look for duplicates
                        for(int i = 0; i < cookieStore.get(url.host()).size(); i++){
                            Cookie k = cookieStore.get(url.host()).get(i);
                            if(c.name().equals(k.name())){
                                cookieStore.get(url.host()).remove(k);
                                i--;
                            } 
                            System.out.println("Removed duplicate cookie " + c.name());
                        }

                         cookieStore.get(url.host()).add(c);

                    }
                } else {
                    cookieStore.put(url.host(), new ArrayList<Cookie>(cookies));
                }

                System.out.println(cookieStore);
            }
    
            @NotNull
            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }

    
}
