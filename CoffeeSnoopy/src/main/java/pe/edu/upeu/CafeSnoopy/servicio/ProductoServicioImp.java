package pe.edu.upeu.CafeSnoopy.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.CafeSnoopy.modelo.Producto;
import pe.edu.upeu.CafeSnoopy.repositorio.ProductoRepository;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServicioImp implements ProductoServicioI {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto update(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void delete(String codigo) {
        productoRepository.deleteById(codigo);
    }

    @Override
    public Optional<Producto> findById(String codigo) {
        return productoRepository.findById(codigo);
    }

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    // ✅ NUEVO: Implementación del método para generar códigos automáticamente
    @Override
    public String generarCodigo() {
        List<Producto> productos = productoRepository.findAll();

        // 1. Buscar el número más alto existente
        int max = productos.stream()
                .map(p -> {
                    try {
                        // Extraemos solo los números del código (ej: "PROD-005" -> 5)
                        // "\\D" es una expresión regular que significa "todo lo que NO es dígito"
                        return Integer.parseInt(p.getCodigo().replaceAll("\\D", ""));
                    } catch (Exception e) {
                        // Si el código no tiene formato numérico (ej: "CAFE"), lo ignoramos retornando 0
                        return 0;
                    }
                })
                .max(Integer::compare)
                .orElse(0); // Si no hay productos, empezamos desde 0

        // 2. Generar el siguiente número con formato (ej: 6 -> "PROD-006")
        return String.format("PROD-%03d", max + 1);
    }
}