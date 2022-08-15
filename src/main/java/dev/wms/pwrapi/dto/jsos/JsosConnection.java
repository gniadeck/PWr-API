package dev.wms.pwrapi.dto.jsos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsosConnection {

    private String sessionID;
    private String oauthConsumerKey;
    private String oauthToken;
    private String YII_CSRF_TOKEN;
    private String oauthSessionToken;
    
}
