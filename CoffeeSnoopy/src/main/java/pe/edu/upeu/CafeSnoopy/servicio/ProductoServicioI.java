package pe.edu.upeu.CafeSnoopy.servicio;

import pe.edu.upeu.CafeSnoopy.modelo.Producto;
import java.util.List;

public interface ProductoServicioI {
    void save(Producto producto);
    List<Producto> findAll();
    Producto update(Producto producto, int index);
    void delete(int index);
    Producto findById(int index);
}