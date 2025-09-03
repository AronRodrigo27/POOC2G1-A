package pe.edu.upeu.asistencia.modelo;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
//@Data
@Setter
@Getter
public class Estudiante {
    private StringProperty nombre;
    private BooleanProperty estado;

}
