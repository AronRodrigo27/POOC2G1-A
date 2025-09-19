package pe.edu.upeu.asistencia.servicio;

import pe.edu.upeu.asistencia.modelo.Usuario;
import java.util.List;

public interface UsuarioServicioI {
    void save(Usuario usuario);
    List<Usuario> findAll();
    Usuario update(Usuario usuario, int index);
    void delete(int index);
    Usuario findById(int index);
}