package dev.wms.pwrapi.entity.edukacja;

import lombok.*;

@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
@Data
public class Group {
    
    String groupName;
    String teacher;
    String date;
    String form;
    String code;

}
