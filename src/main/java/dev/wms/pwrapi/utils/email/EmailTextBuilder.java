package dev.wms.pwrapi.utils.email;

import dev.wms.pwrapi.utils.common.ResourceLoaderUtils;
import dev.wms.pwrapi.utils.html.HtmlBuilder;

import java.util.Map;

public class EmailTextBuilder {

    private static final String HTML_TEXT_PATH = "templates/email/text.html";
    private static final String HTML_TEXT_WRAPPER_PATH = "templates/email/text_wrapper.html";
    private static final String HTML_TEXT_BUTTON_PATH = "templates/email/text_button.html";

    private final HtmlBuilder htmlBuilder;

    public EmailTextBuilder() {
        this.htmlBuilder = new HtmlBuilder();
    }

    public EmailTextBuilder withText(String text) {
        htmlBuilder.appendAndReplace(
                ResourceLoaderUtils.loadResourceToString(HTML_TEXT_PATH), Map.of("%text%", text)
        );

        return this;
    }

    public EmailTextBuilder withButton(String text, String url) {
        htmlBuilder.appendAndReplace(
                ResourceLoaderUtils.loadResourceToString(HTML_TEXT_BUTTON_PATH),
                Map.of("%text%", text, "%url%", url)
        );

        return this;
    }

    public String build() {
        HtmlBuilder htmlBuilderWrapper = new HtmlBuilder();
        String htmlTemplate = htmlBuilder.build();

        return htmlBuilderWrapper
                .appendAndReplace(
                        ResourceLoaderUtils.loadResourceToString(HTML_TEXT_WRAPPER_PATH),
                        Map.of("%content%", htmlTemplate)
                )
                .build();
    }
}
