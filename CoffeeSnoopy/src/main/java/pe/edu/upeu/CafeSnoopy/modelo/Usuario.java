package pe.edu.upeu.CafeSnoopy.modelo;

import javafx.beans.property.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Usuario {
    private StringProperty username;
    private StringProperty password;
    private StringProperty nombres;
    private StringProperty apellidos;
    private StringProperty email;
    private BooleanProperty estado;

    @Override
    public String toString() {
        return username.get() + " - " + nombres.get() + " " + apellidos.get();
    }
}