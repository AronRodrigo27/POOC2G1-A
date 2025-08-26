package pe.edu.upeu.herencia;

public class Carro extends Vehiculo{
    String modelo="MUSTANG";
    String placa="PE -2309";
    String color="black";

    void caracteristicas(){
        System.out.println("Las caracteristicas de este carrro son ");
        System.out.println("Marca: "+marca);
        System.out.println("Modelo: "+modelo);
        System.out.println("Placa: "+placa);
        System.out.println("Color: "+color);

    }
    public static void main(String[] args) {
        Carro carro = new Carro();
        carro.caracteristicas();
        carro.sonido();
    }
}
