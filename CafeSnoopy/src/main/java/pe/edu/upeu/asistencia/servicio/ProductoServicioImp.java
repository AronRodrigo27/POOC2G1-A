package pe.edu.upeu.asistencia.servicio;

import org.springframework.stereotype.Service;
import pe.edu.upeu.asistencia.modelo.Producto;
import pe.edu.upeu.asistencia.repositorio.ProductoRepository;
import java.util.List;

@Service
public class ProductoServicioImp extends ProductoRepository implements ProductoServicioI {

    @Override
    public void save(Producto producto) {
        productos.add(producto);
    }

    @Override
    public Producto update(Producto producto, int index) {
        return productos.set(index, producto);
    }

    @Override
    public void delete(int index) {
        productos.remove(index);
    }

    @Override
    public Producto findById(int index) {
        return productos.get(index);
    }

    @Override
    public List<Producto> findAll() {
        if (productos.isEmpty()) {
            return super.findAll();
        }
        return productos;
    }
}