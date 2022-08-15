package dev.wms.pwrapi.entity.jsos.marks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@lombok.Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JsosMark {

    private String prowadzacy;
    private String kodKursu;
    private String nazwaKursu;
    private String formaZajec;
    private String ZZU;
    private String ocena;
    private String data;
    private String ects;
    
}
