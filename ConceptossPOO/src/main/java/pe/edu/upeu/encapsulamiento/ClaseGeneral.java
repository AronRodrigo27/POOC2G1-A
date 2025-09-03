package pe.edu.upeu.encapsulamiento;


public class ClaseGeneral {
    public static void probarJugador(){
        Jugador jugador = new Jugador();
        jugador.setNombre("CR7");
        jugador.setApellido("PORtugal");
        jugador.setEdad(25);
        jugador.setPosicion("delantero");
        jugador.setNumeroCam(7);

        System.out.println(jugador);

    }


    public static void probar(){
        Estudiante estudiante = new Estudiante();
        estudiante.setCarrera("ing Sistemas");
        estudiante.setCodigo("202510878");
        estudiante.trabajo();
        //System.out.println(estudiante);

    }




    public static void main(String[] args) {
        Persona persona = new Persona();//objeto

        persona.setNombre("Aron");//acceso al atributo,sed para poner valor y get para poner valor
        persona.setEdad(17);
        persona.setGenero('M');
        persona.saludo();

        System.out.println("Nombre: "+persona.getNombre());


        probar();
        probarJugador();

    }
}
