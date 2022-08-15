package dev.wms.pwrapi.dto.eportal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class userDetails {
   
    private String username;
    private int userID;

}
