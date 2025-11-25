package pe.edu.upeu.CafeSnoopy.servicio;

import pe.edu.upeu.CafeSnoopy.modelo.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaServicioI {
    Categoria save(Categoria categoria);
    Categoria update(Categoria categoria);
    void delete(Integer id);
    Optional<Categoria> findById(Integer id);
    List<Categoria> findAll();
}