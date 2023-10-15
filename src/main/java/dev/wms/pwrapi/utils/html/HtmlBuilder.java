package dev.wms.pwrapi.utils.html;

import java.util.Map;


public class HtmlBuilder {
    private final StringBuilder strBuilder;

    public HtmlBuilder() {
        strBuilder = new StringBuilder();
    }

    /**
     * @param html - html template as string
     */
    public HtmlBuilder append(String html) {
        strBuilder.append(html);
        return this;
    }

    /**
     * @param html - html template as string
     * @param keysToReplace - map of keys to replace in html template like {%url%: "someUrl"}
     * */
    public HtmlBuilder appendAndReplace(String html, Map<String, String> keysToReplace){
        strBuilder.append(replace(html, keysToReplace));
        return this;
    }

    public String build() {
        return strBuilder.toString();
    }

    public static String replace(String html, Map<String, String> keysToReplace) {
        for(Map.Entry<String, String> entry : keysToReplace.entrySet()){
            html = html.replace(entry.getKey(), entry.getValue());
        }
        return html;
    }
}