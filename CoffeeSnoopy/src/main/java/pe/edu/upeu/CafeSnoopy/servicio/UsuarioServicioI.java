package pe.edu.upeu.CafeSnoopy.servicio;

import pe.edu.upeu.CafeSnoopy.modelo.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioServicioI {
    Usuario save(Usuario usuario);
    Usuario update(Usuario usuario);
    void delete(Integer id);
    Optional<Usuario> findById(Integer id);
    List<Usuario> findAll();
    Optional<Usuario> findByUsername(String username); // Nuevo método para Login
}