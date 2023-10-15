package dev.wms.pwrapi.utils.email;

import dev.wms.pwrapi.service.html.MarkdownService;
import dev.wms.pwrapi.service.internationalization.LocalizedMessageService;
import dev.wms.pwrapi.service.internationalization.SupportedLanguage;
import dev.wms.pwrapi.utils.common.ResourceLoaderUtils;
import dev.wms.pwrapi.utils.html.HtmlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailUtils {

    private final MarkdownService markdownService;
    private final LocalizedMessageService msgService;

    private static final String HTML_HEADER_PATH = "templates/email/banner.html";
    private static final String HTML_FOOTER_PATH = "templates/email/footer.html";
    private static final String HTML_WRAPPER_PATH = "templates/email/wrapper.html";


    /**
     * Creates an email template with header, footer and text created with Markdown.
     * @param markdownText text to be converted into html using Markdown
     * */
    public String createTextMailTemplate(String markdownText){
        String htmlText = new EmailTextBuilder()
                .withText(markdownService.toHtmlWithMarkdowns(markdownText))
                .build();

        return wrapIntoEmail(htmlText);
    }

    public String createReceiveApiKeyEmailTemplate(String rawApiKey) {
        String text = msgService.getMessageWithArgs(
                "msg.mail.body.api-key",
                SupportedLanguage.EN,
                rawApiKey
        );
        String markdownHtml = markdownService.toHtmlWithMarkdowns(text);

        String htmlText = new EmailTextBuilder()
                .withText(markdownHtml)
                .build();

        return wrapIntoEmail(htmlText);
    }

    public String createEmailConfirmationEmailTemplate(String urlRedirect) {
        String text = msgService.getMessage(
                "msg.mail.body.confirm-email",
                SupportedLanguage.EN
        );

        String markdownHtml = markdownService.toHtmlWithMarkdowns(text);

        String html = new EmailTextBuilder()
                .withText(markdownHtml)
                .withButton("Confirm email address", urlRedirect)
                .build();

        return wrapIntoEmail(html);
    }

    private String wrapContent(String htmlTemplate, String wrapperPath) {
        HtmlBuilder htmlBuilder = new HtmlBuilder();

        Map<String, String> textKeys = Map.of(
                "%content%", htmlTemplate
        );

        return htmlBuilder
                .appendAndReplace(ResourceLoaderUtils.loadResourceToString(wrapperPath), textKeys)
                .build();
    }

    /**
     * Wraps the body of the email, so it has header and footer
     */
    private String wrapIntoEmail(String htmlBody) {
        HtmlBuilder htmlBuilder = new HtmlBuilder();

        String htmlHeader = ResourceLoaderUtils.loadResourceToString(HTML_HEADER_PATH);
        String htmlFooter = createFooter();

        String content = htmlBuilder
                .append(htmlHeader)
                .append(htmlBody)
                .append(htmlFooter)
                .build();

        return wrapContent(content, HTML_WRAPPER_PATH);
    }

    private String createFooter() {
        String footer = ResourceLoaderUtils.loadResourceToString(HTML_FOOTER_PATH);
        return HtmlBuilder.replace(footer, Map.of("%random_text%", LocalDateTime.now().toString()));
    }
}

