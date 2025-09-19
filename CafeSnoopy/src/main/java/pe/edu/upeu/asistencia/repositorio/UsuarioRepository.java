package pe.edu.upeu.asistencia.repositorio;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import pe.edu.upeu.asistencia.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;

public abstract class UsuarioRepository {
    protected List<Usuario> usuarios = new ArrayList<>();

    public List<Usuario> findAll() {
        if (usuarios.isEmpty()) {
            // Datos de ejemplo
            usuarios.add(new Usuario(
                    new SimpleStringProperty("Wagner.chambi"),
                    new SimpleStringProperty("202510050"),
                    new SimpleStringProperty("Yoel Wagner"),
                    new SimpleStringProperty("Chambi Sirena"),
                    new SimpleStringProperty("wagner.chambi@upeu.edu.pe"),
                    new SimpleBooleanProperty(true)
            ));
            usuarios.add(new Usuario(
                    new SimpleStringProperty("Aron.q.magaño"),
                    new SimpleStringProperty("202510878"),
                    new SimpleStringProperty("Aron Rodrigo"),
                    new SimpleStringProperty("Magaño Quispe"),
                    new SimpleStringProperty("aron.m.quispe@upeu.edu.pe"),
                    new SimpleBooleanProperty(true)
            ));
        }
        return usuarios;
    }
}