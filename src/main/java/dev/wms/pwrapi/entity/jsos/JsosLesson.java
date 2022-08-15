package dev.wms.pwrapi.entity.jsos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JsosLesson {

    private String idKursu;
    private String nazwaKursu;
    private String prowadzacy;
    private String kodGrupy;
    private String termin;
    private String godziny;
    private String ects;
    
}
