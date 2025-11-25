package pe.edu.upeu.CafeSnoopy.servicio;

import pe.edu.upeu.CafeSnoopy.modelo.DetalleVenta;
import java.util.List;

public interface DetalleVentaService {
    List<DetalleVenta> listarPorVenta(Integer idVenta);
    DetalleVenta guardar(DetalleVenta detalleVenta);
}