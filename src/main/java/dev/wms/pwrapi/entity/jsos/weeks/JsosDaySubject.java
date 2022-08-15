package dev.wms.pwrapi.entity.jsos.weeks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class JsosDaySubject {
    
    private String data;
    private String nazwaPrzedmiotu;
    private String lokalizacja;
    private String prowadzacy;
    private String kodGrupy;
    private String liczbaZapisanych;
    private String type;


}
