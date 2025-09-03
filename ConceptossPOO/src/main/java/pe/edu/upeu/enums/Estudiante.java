package pe.edu.upeu.enums;
enum GENERO{MASCULINO,FEMENINO}
public class Estudiante {
    String codigo;
    String nombre;
    String apellido;
    GENERO genero;
    Carerra carrera;
    public Estudiante(String codigo, String nombre,
                      String apellido,Carerra carrera,
                      GENERO genero){
        this.codigo=codigo;
        this.nombre=nombre;
        this.apellido=apellido;
        this.genero=genero;
        this.carrera=carrera;
    }

    public static void main(String[] args){
        Estudiante e1=new Estudiante("202510878","Aron",
                "Magaño",Carerra.Sistemas,GENERO.MASCULINO);
        System.out.println(e1.codigo+" "+e1.genero+" "+e1.carrera+" "+e1.apellido+" "+e1.nombre);
        for(Carerra c:Carerra.values()){
            System.out.println(c);
        }

    }
}
