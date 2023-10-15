package dev.wms.pwrapi.service.html;

import dev.wms.pwrapi.service.html.styling.HeadingAttributeProvider;
import dev.wms.pwrapi.service.html.styling.TextAttributeProvider;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

@Service
public class MarkdownServiceImpl implements MarkdownService {

    private final Parser parser;
    private final HtmlRenderer renderer;

    public MarkdownServiceImpl() {
        this.parser = Parser.builder().build();

        this.renderer = HtmlRenderer.builder()
                .attributeProviderFactory(attributeProviderContext -> new HeadingAttributeProvider())
                .attributeProviderFactory(attributeProviderContext -> new TextAttributeProvider())
                .build();
    }

    @Override
    public String toHtmlWithMarkdowns(String markdownText) {
        Node document = parser.parse(markdownText);
        return renderer.render(document);
    }
}
