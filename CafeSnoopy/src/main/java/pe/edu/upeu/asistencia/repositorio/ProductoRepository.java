package pe.edu.upeu.asistencia.repositorio;

import javafx.beans.property.*;
import pe.edu.upeu.asistencia.modelo.Producto;
import java.util.ArrayList;
import java.util.List;

public abstract class ProductoRepository {
    protected List<Producto> productos = new ArrayList<>();

    public List<Producto> findAll() {
        if (productos.isEmpty()) {
            // Datos de ejemplo
            productos.add(new Producto(
                    new SimpleStringProperty("PROD001"),
                    new SimpleStringProperty("Laptop HP"),
                    new SimpleStringProperty("Laptop HP 15.6 pulgadas, 8GB RAM"),
                    new SimpleDoubleProperty(2500.00),
                    new SimpleIntegerProperty(10),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("PROD002"),
                    new SimpleStringProperty("Mouse Inalámbrico"),
                    new SimpleStringProperty("Mouse óptico inalámbrico"),
                    new SimpleDoubleProperty(45.00),
                    new SimpleIntegerProperty(25),
                    new SimpleBooleanProperty(true)
            ));
        }
        return productos;
    }
}