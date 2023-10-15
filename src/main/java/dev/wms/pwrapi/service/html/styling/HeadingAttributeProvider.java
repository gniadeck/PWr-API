package dev.wms.pwrapi.service.html.styling;

import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.AttributeProvider;

import java.util.Map;

public class HeadingAttributeProvider implements AttributeProvider {

    @Override
    public void setAttributes(Node node, String tag, Map<String, String> map) {
        if(node instanceof Heading){
            String commonStyle = "width: 100%; text-align: center; color: black;";

            switch (tag) {
                case "h1" -> map.put("style", commonStyle + "font-size: 2rem;");
                case "h2" -> map.put("style", commonStyle + "font-size: 1.75rem;");
                case "h3" -> map.put("style", commonStyle + "font-size: 1.5rem;");
                case "h4" -> map.put("style", commonStyle + "font-size: 1.25rem;");
                case "h5" -> map.put("style", commonStyle + "font-size: 1rem;");
                case "h6" -> map.put("style", commonStyle + "font-size: 0.80rem;");
            }
        }
    }
}
