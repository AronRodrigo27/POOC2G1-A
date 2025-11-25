package pe.edu.upeu.CafeSnoopy.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.CafeSnoopy.modelo.Producto;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {

    // Buscar productos por nombre o código para la venta
    @Query("SELECT p FROM Producto p WHERE lower(p.nombre) LIKE lower(concat('%', :filtro, '%')) OR lower(p.codigo) LIKE lower(concat('%', :filtro, '%'))")
    List<Producto> buscarPorNombreOCodigo(String filtro);

    // Estadísticas: Valor total del inventario (Precio * Stock)
    @Query("SELECT SUM(p.precio * p.stock) FROM Producto p")
    BigDecimal obtenerValorTotalInventario();

    // Estadísticas: Contar productos con stock bajo (ejemplo: menos de 5)
    long countByStockLessThan(int stockMinimo);
}