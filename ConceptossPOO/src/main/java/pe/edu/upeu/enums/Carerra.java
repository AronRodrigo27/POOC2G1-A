package pe.edu.upeu.enums;

enum FACULTAD{
    FIA,
    FCE,
    FACHIED,
    FCS
}

public enum Carerra {
    Sistemas(FACULTAD.FIA),
    Civil(FACULTAD.FIA),
    Ambiental(FACULTAD.FIA),
    Industrial(FACULTAD.FIA);



    FACULTAD facultad;
    Carerra(FACULTAD facultad){
        this.facultad=facultad;
    }
}
