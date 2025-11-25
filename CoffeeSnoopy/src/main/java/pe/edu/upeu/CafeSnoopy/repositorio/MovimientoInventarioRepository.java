package pe.edu.upeu.CafeSnoopy.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.CafeSnoopy.modelo.MovimientoInventario;
import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Integer> {

    List<MovimientoInventario> findByProducto_Codigo(String codigoProducto);

    // Consulta para sumar cantidades por tipo de movimiento HOY (Entradas, Salidas, Ajustes)
    @Query(value = "SELECT COALESCE(SUM(cantidad), 0) FROM movimiento_inventario " +
            "WHERE tipo_movimiento = :tipo AND date(fecha_movimiento) = date('now', 'localtime')",
            nativeQuery = true)
    Integer sumarCantidadPorTipoHoy(@Param("tipo") String tipo);
}