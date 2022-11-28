package dev.wms.pwrapi.dto.news;

public enum FacultyType {

    INFORMATYKI_I_TELEKOMUNIKACJI("https://wit.pwr.edu.pl/rss/pl/189.xml", true),
    ZARZADZANIA("https://wz.pwr.edu.pl/rss/pl/127.xml", true),
    ELEKTRONIKI_MIKROSYSTEMOW_I_FOTONIKI("https://wefim.pwr.edu.pl/rss/pl/5.xml", true),
    ARCHITEKTURY("https://wa.pwr.edu.pl/o-wydziale/aktualnosci", false),
    BUDOWNICTWA_LADOWEGO_I_WODNEGO("https://wbliw.pwr.edu.pl/o-wydziale/aktualnosci", false),
    CHEMICZNY("https://wch.pwr.edu.pl/o-wydziale/aktualnosci", false),
    ELEKTRYCZNY("https://weny.pwr.edu.pl/o-wydziale/aktualnosci", false),
    GEOINZYNIERII_GORNICTWA_I_GEOLOGII("https://wggg.pwr.edu.pl/o-wydziale/aktualnosci", false),
    INZYNIERII_SRODOWISKA("https://wis.pwr.edu.pl/o-wydziale/aktualnosci", false),
    MECHANICZNO_ENERGETYCZNY("https://wme.pwr.edu.pl/aktualnosci", false),
    MECHANICZNY("https://wm.pwr.edu.pl/o-wydziale/aktualnosci", false),
    PODSTAWOWYCH_PROBLEMOW_TECHNIKI("https://wppt.pwr.edu.pl/o-wydziale/aktualnosci", false),
    MATEMATYKI("https://wmat.pwr.edu.pl/o-wydziale/aktualnosci", false),
    FILIA_POLITECHNIKI_WROCLAWSKIEJ_W_JELENIEJ_GORZE("https://jelenia-gora.pwr.edu.pl/o-filii/aktualnosci", false),
    FILIA_POLITECHNIKI_WROCLAWSKIEJ_W_WALBRZYCHU("https://walbrzych.pwr.edu.pl/o-filii/aktualnosci", false),
    FILIA_POLITECHNIKI_WROCLAWSKIEJ_W_LEGNICY("https://legnica.pwr.edu.pl/o-wydziale/aktualnosci", false);



    public final String url;
    public final boolean isRss;

    FacultyType(String url, boolean isRss) {
        this.url = url;
        this.isRss = isRss;
    }
}
