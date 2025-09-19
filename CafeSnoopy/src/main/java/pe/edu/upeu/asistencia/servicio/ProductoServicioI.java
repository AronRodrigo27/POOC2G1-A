package pe.edu.upeu.asistencia.servicio;

import pe.edu.upeu.asistencia.modelo.Producto;
import java.util.List;

public interface ProductoServicioI {
    void save(Producto producto);
    List<Producto> findAll();
    Producto update(Producto producto, int index);
    void delete(int index);
    Producto findById(int index);
}