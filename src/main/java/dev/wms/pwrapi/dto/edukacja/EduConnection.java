package dev.wms.pwrapi.dto.edukacja;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EduConnection {

    private String sessionToken;
    private String webToken;
    private String jsessionid;



    
}
