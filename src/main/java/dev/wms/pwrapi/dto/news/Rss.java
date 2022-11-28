package dev.wms.pwrapi.dto.news;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rss {
    public Channel channel;
    public double version;
    public String text;
}
