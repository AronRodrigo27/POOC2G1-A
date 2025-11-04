package pe.edu.upeu.CafeSnoopy.modelo;

import javafx.beans.property.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Producto {
    private StringProperty codigo;
    private StringProperty nombre;
    private StringProperty descripcion;
    private DoubleProperty precio;
    private IntegerProperty stock;
    private BooleanProperty estado;

    @Override
    public String toString() {
        return codigo.get() + " - " + nombre.get();
    }
}