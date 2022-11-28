package dev.wms.pwrapi.dto.news;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Channel {
    public String title;
    public String link;
    public String description;
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<Item> item;
}
