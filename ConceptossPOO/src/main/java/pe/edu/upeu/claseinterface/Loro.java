package pe.edu.upeu.claseinterface;

public class Loro implements Animal {

    @Override
    public void emitirSonido() {
        System.out.println("hola cumpa");
    }

    @Override
    public void dormir() {
        System.out.println("zzzzzzzz............zzz");
    }
}
