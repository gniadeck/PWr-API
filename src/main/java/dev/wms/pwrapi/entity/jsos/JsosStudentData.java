package dev.wms.pwrapi.entity.jsos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@lombok.Builder
public class JsosStudentData {
    
    private String wydzial;
    private String kierunek;
    private String specjalnosc;
    private String stopien;
    private String numberAlbumu;
    private String imiona;
    private String nazwisko;
    private String imieOjca;
    private String dataUrodzenia;
    private String miejsceUrodzenia;


}
