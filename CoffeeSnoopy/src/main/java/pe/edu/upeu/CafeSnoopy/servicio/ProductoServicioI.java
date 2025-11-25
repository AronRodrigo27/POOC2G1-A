package pe.edu.upeu.CafeSnoopy.servicio;

import pe.edu.upeu.CafeSnoopy.modelo.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoServicioI {
    Producto save(Producto producto);
    Producto update(Producto producto);
    void delete(String codigo);
    Optional<Producto> findById(String codigo);
    List<Producto> findAll();

    // ✅ NUEVO: Método para generar códigos
    String generarCodigo();
}