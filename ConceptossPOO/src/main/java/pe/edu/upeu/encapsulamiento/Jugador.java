package pe.edu.upeu.encapsulamiento;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Jugador {
    private String nombre;
    private String apellido;
    private int edad;
    private String posicion;
    private int numeroCam;

    public String toString() {
        return "el jugador tiene estas caracteristicas :\n"+
                "nombre:"+nombre+"\n"
                +"Apellido:"+apellido+"\n"
                +"Edad:"+edad+"\n"
                +"Posicion:"+posicion+"\n"
                +"numeroCam:"+numeroCam+"\n"
                ;
    }

}
