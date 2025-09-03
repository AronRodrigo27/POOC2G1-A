package pe.edu.upeu.abspolimorfismo;

public class Loro extends Animal{
    private String tipo ="loro";

    @Override
    public void emitirSonido(){
        System.out.println("Hey.....no te  vayas a mimir...atiende ");
    }

    @Override
    public void dormir() {
        super.dormir();// lo que se hereda metodos "super"...dis por si hay varios co un mismo nombre
        System.out.println(super.tipo);
        System.out.println(this.tipo);
        System.out.println("no molestar....dejem dormir");
    }
}
