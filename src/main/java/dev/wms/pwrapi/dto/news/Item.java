package dev.wms.pwrapi.dto.news;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    public String title;
    public String link;
    public String pubDate;
    public String description;
}

