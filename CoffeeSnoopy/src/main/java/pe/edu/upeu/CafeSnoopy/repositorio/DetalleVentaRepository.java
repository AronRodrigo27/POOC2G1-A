package pe.edu.upeu.CafeSnoopy.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.CafeSnoopy.modelo.DetalleVenta;
import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {

    // Buscar detalles por ID de venta (lo usa el servicio de detalle)
    List<DetalleVenta> findByVenta_IdVenta(Integer idVenta);

    // 1. Total de productos vendidos HOY (Para las tarjetas de métricas)
    @Query(value = "SELECT COALESCE(SUM(dv.cantidad), 0) " +
            "FROM detalle_venta dv " +
            "JOIN venta v ON dv.id_venta = v.id_venta " +
            "WHERE date(v.fecha_venta) = date('now', 'localtime')", nativeQuery = true)
    Integer contarProductosVendidosHoy();

    // 2. Top 5 Productos más vendidos (Para la tabla de 'Productos Más Vendidos')
    @Query(value = "SELECT p.nombre, SUM(dv.cantidad) as total_cantidad, SUM(dv.subtotal) as total_dinero " +
            "FROM detalle_venta dv " +
            "JOIN producto p ON dv.id_producto = p.codigo " +
            "GROUP BY p.nombre " +
            "ORDER BY total_cantidad DESC LIMIT 5", nativeQuery = true)
    List<Object[]> obtenerProductosMasVendidos();
}