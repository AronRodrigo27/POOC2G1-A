package pe.edu.upeu.claseinterface;

public class Perro implements Animal {

    @Override
    public void emitirSonido() {
        System.out.println("guauuuuu cumpa....");
    }

    @Override
    public void dormir() {
        System.out.println("zzzzzzzz............zzz");
    }
}
