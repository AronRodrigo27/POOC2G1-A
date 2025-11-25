package pe.edu.upeu.CafeSnoopy.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.CafeSnoopy.modelo.Usuario;
import pe.edu.upeu.CafeSnoopy.repositorio.UsuarioRepository;
import java.util.List;
import java.util.Optional;

@Service
// ⚠️ ¡Ya no extiende UsuarioRepository!
public class UsuarioServicioImp implements UsuarioServicioI {

    @Autowired
    private UsuarioRepository usuarioRepository; // ⬅️ Inyección de JpaRepository

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario update(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public void delete(Integer id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }
}