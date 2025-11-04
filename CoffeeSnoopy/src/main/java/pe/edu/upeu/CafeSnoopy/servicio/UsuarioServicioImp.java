package pe.edu.upeu.CafeSnoopy.servicio;

import org.springframework.stereotype.Service;
import pe.edu.upeu.CafeSnoopy.modelo.Usuario;
import pe.edu.upeu.CafeSnoopy.repositorio.UsuarioRepository;
import java.util.List;

@Service
public class UsuarioServicioImp extends UsuarioRepository implements UsuarioServicioI {

    @Override
    public void save(Usuario usuario) {
        usuarios.add(usuario);
    }

    @Override
    public Usuario update(Usuario usuario, int index) {
        return usuarios.set(index, usuario);
    }

    @Override
    public void delete(int index) {
        usuarios.remove(index);
    }

    @Override
    public Usuario findById(int index) {
        return usuarios.get(index);
    }

    @Override
    public List<Usuario> findAll() {
        if (usuarios.isEmpty()) {
            return super.findAll();
        }
        return usuarios;
    }
}