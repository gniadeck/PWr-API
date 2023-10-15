package dev.wms.pwrapi.service.html.styling;

import org.commonmark.node.Node;
import org.commonmark.node.Paragraph;
import org.commonmark.renderer.html.AttributeProvider;

import java.util.Map;

public class TextAttributeProvider implements AttributeProvider {
    @Override
    public void setAttributes(Node node, String s, Map<String, String> map) {
        if(node instanceof Paragraph){
            map.put("style", "color: black; font-size: 0.75rem; text-align: justify;");
        }
    }
}
