package pe.edu.upeu.CafeSnoopy.servicio;

import pe.edu.upeu.CafeSnoopy.modelo.Venta;
import java.util.List;
import java.util.Optional;

public interface VentaServicioI {
    List<Venta> listarTodos();
    Optional<Venta> buscarPorId(Integer id);
    Venta guardar(Venta venta);
    void eliminar(Integer id);
    List<Venta> buscarPorFecha(java.time.LocalDate fecha);
}