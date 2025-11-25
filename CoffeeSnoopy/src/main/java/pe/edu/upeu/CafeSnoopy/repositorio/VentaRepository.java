package pe.edu.upeu.CafeSnoopy.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.CafeSnoopy.modelo.Venta;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {

    // 1. Suma total de dinero vendido HOY (Para Métricas y Dashboard)
    // Usa COALESCE para que devuelva 0 si no hay ventas, en lugar de null (que daría error)
    @Query(value = "SELECT COALESCE(SUM(total_venta), 0) FROM venta WHERE date(fecha_venta) = date('now', 'localtime')", nativeQuery = true)
    BigDecimal sumaVentasHoy();

    // 2. Cantidad de ventas realizadas HOY
    @Query(value = "SELECT COUNT(*) FROM venta WHERE date(fecha_venta) = date('now', 'localtime')", nativeQuery = true)
    Integer contarVentasHoy();

    // 3. Datos para el Gráfico de Barras (Ventas de los últimos 7 días)
    @Query(value = "SELECT date(fecha_venta) as fecha, SUM(total_venta) as total FROM venta GROUP BY date(fecha_venta) ORDER BY date(fecha_venta) DESC LIMIT 7", nativeQuery = true)
    List<Object[]> reporteVentasUltimaSemana();

    // 4. Buscar por rango de fechas (Para el Historial y filtros)
    @Query(value = "SELECT * FROM venta WHERE date(fecha_venta) BETWEEN :fechaInicio AND :fechaFin", nativeQuery = true)
    List<Venta> buscarPorRangoFechas(@Param("fechaInicio") String fechaInicio, @Param("fechaFin") String fechaFin);

    // 5. Datos para Reporte por Categoría (Gráfico Pastel)
    @Query(value = "SELECT c.nombre_categoria, SUM(d.cantidad) as total " +
            "FROM detalle_venta d " +
            "JOIN producto p ON d.id_producto = p.codigo " +
            "JOIN categoria c ON p.id_categoria = c.id_categoria " +
            "GROUP BY c.nombre_categoria", nativeQuery = true)
    List<Object[]> obtenerVentasPorCategoria();
}