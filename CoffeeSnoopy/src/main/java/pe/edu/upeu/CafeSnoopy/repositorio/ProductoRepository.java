package pe.edu.upeu.CafeSnoopy.repositorio;

import javafx.beans.property.*;
import pe.edu.upeu.CafeSnoopy.modelo.Producto;
import java.util.ArrayList;
import java.util.List;

public abstract class ProductoRepository {
    protected List<Producto> productos = new ArrayList<>();

    public List<Producto> findAll() {
        if (productos.isEmpty()) {
            // Café y Bebidas Calientes
            productos.add(new Producto(
                    new SimpleStringProperty("CAFE001"),
                    new SimpleStringProperty("Café Americano (350ml)"),
                    new SimpleStringProperty("Café negro tradicional de grano arábica - 350ml"),
                    new SimpleDoubleProperty(8.50),
                    new SimpleIntegerProperty(50),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("CAFE002"),
                    new SimpleStringProperty("Cappuccino Clásico (300ml)"),
                    new SimpleStringProperty("Café espresso con leche vaporizada y espuma - 300ml"),
                    new SimpleDoubleProperty(12.00),
                    new SimpleIntegerProperty(35),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("CAFE003"),
                    new SimpleStringProperty("Latte Snoopy (400ml)"),
                    new SimpleStringProperty("Latte con arte de Snoopy en la espuma - 400ml"),
                    new SimpleDoubleProperty(14.50),
                    new SimpleIntegerProperty(25),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("CAFE004"),
                    new SimpleStringProperty("Mocha Chocolate (350ml)"),
                    new SimpleStringProperty("Café con chocolate y crema batida - 350ml"),
                    new SimpleDoubleProperty(15.00),
                    new SimpleIntegerProperty(30),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("CAFE005"),
                    new SimpleStringProperty("Expresso Doble (60ml)"),
                    new SimpleStringProperty("Doble shot de café expresso intenso - 60ml"),
                    new SimpleDoubleProperty(10.00),
                    new SimpleIntegerProperty(40),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("CAFE006"),
                    new SimpleStringProperty("Café con Leche (400ml)"),
                    new SimpleStringProperty("Café con leche vaporizada - 400ml"),
                    new SimpleDoubleProperty(9.50),
                    new SimpleIntegerProperty(45),
                    new SimpleBooleanProperty(true)
            ));

            // Bebidas Frías
            productos.add(new Producto(
                    new SimpleStringProperty("FRIO001"),
                    new SimpleStringProperty("Iced Coffee (500ml)"),
                    new SimpleStringProperty("Café frío con hielo y leche - 500ml"),
                    new SimpleDoubleProperty(11.00),
                    new SimpleIntegerProperty(20),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("FRIO002"),
                    new SimpleStringProperty("Frappé de Vainilla (450ml)"),
                    new SimpleStringProperty("Bebida fría mezclada con vainilla - 450ml"),
                    new SimpleDoubleProperty(16.50),
                    new SimpleIntegerProperty(15),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("FRIO003"),
                    new SimpleStringProperty("Té Helado Limón (500ml)"),
                    new SimpleStringProperty("Té negro helado con limón natural - 500ml"),
                    new SimpleDoubleProperty(9.00),
                    new SimpleIntegerProperty(25),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("FRIO004"),
                    new SimpleStringProperty("Smoothie Frutilla (450ml)"),
                    new SimpleStringProperty("Smoothie de frutilla natural - 450ml"),
                    new SimpleDoubleProperty(18.00),
                    new SimpleIntegerProperty(18),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("FRIO005"),
                    new SimpleStringProperty("Cold Brew (350ml)"),
                    new SimpleStringProperty("Café infusionado en frío - 350ml"),
                    new SimpleDoubleProperty(13.50),
                    new SimpleIntegerProperty(22),
                    new SimpleBooleanProperty(true)
            ));

            // Pastelería y Postres
            productos.add(new Producto(
                    new SimpleStringProperty("POST001"),
                    new SimpleStringProperty("Croissant Mantequilla (80g)"),
                    new SimpleStringProperty("Croissant artesanal de mantequilla - 80g"),
                    new SimpleDoubleProperty(7.50),
                    new SimpleIntegerProperty(20),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("POST002"),
                    new SimpleStringProperty("Cheesecake Frambuesa (120g)"),
                    new SimpleStringProperty("Porción de cheesecake con salsa de frambuesa - 120g"),
                    new SimpleDoubleProperty(18.00),
                    new SimpleIntegerProperty(12),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("POST003"),
                    new SimpleStringProperty("Brownie Chocolate (100g)"),
                    new SimpleStringProperty("Brownie de chocolate con nueces - 100g"),
                    new SimpleDoubleProperty(12.50),
                    new SimpleIntegerProperty(18),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("POST004"),
                    new SimpleStringProperty("Galletas Avena (60g)"),
                    new SimpleStringProperty("Galletas de avena con pasas y chocolate - 60g (2 unidades)"),
                    new SimpleDoubleProperty(6.00),
                    new SimpleIntegerProperty(30),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("POST005"),
                    new SimpleStringProperty("Muffin Arándanos (90g)"),
                    new SimpleStringProperty("Muffin de arándanos frescos - 90g"),
                    new SimpleDoubleProperty(8.50),
                    new SimpleIntegerProperty(25),
                    new SimpleBooleanProperty(true)
            ));

            // Sandwiches y Comida Ligera
            productos.add(new Producto(
                    new SimpleStringProperty("SAND001"),
                    new SimpleStringProperty("Sandwich Club (250g)"),
                    new SimpleStringProperty("Sandwich de pollo, tocino y aguacate - 250g"),
                    new SimpleDoubleProperty(22.00),
                    new SimpleIntegerProperty(15),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("SAND002"),
                    new SimpleStringProperty("Bagel Queso Crema (180g)"),
                    new SimpleStringProperty("Bagel con queso crema y salmón - 180g"),
                    new SimpleDoubleProperty(19.50),
                    new SimpleIntegerProperty(10),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("SAND003"),
                    new SimpleStringProperty("Ensalada César (300g)"),
                    new SimpleStringProperty("Ensalada con pollo, crutones y aderezo césar - 300g"),
                    new SimpleDoubleProperty(20.00),
                    new SimpleIntegerProperty(8),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("SAND004"),
                    new SimpleStringProperty("Wrap Vegetariano (220g)"),
                    new SimpleStringProperty("Wrap con vegetales frescos y hummus - 220g"),
                    new SimpleDoubleProperty(16.50),
                    new SimpleIntegerProperty(12),
                    new SimpleBooleanProperty(true)
            ));

            // Productos Empaquetados
            productos.add(new Producto(
                    new SimpleStringProperty("EMP001"),
                    new SimpleStringProperty("Café Molido 250g"),
                    new SimpleStringProperty("Bolsa de café molido grano arábica - 250g"),
                    new SimpleDoubleProperty(45.00),
                    new SimpleIntegerProperty(25),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("EMP002"),
                    new SimpleStringProperty("Taza Snoopy (350ml)"),
                    new SimpleStringProperty("Taza oficial de Café Snoopy - capacidad 350ml"),
                    new SimpleDoubleProperty(35.00),
                    new SimpleIntegerProperty(15),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("EMP003"),
                    new SimpleStringProperty("Chocolate Caliente 200g"),
                    new SimpleStringProperty("Chocolate en polvo para preparar - 200g"),
                    new SimpleDoubleProperty(28.00),
                    new SimpleIntegerProperty(20),
                    new SimpleBooleanProperty(true)
            ));
            productos.add(new Producto(
                    new SimpleStringProperty("EMP004"),
                    new SimpleStringProperty("Jarabe Vainilla 250ml"),
                    new SimpleStringProperty("Jarabe de vainilla para café - 250ml"),
                    new SimpleDoubleProperty(22.00),
                    new SimpleIntegerProperty(18),
                    new SimpleBooleanProperty(true)
            ));
        }
        return productos;
    }
}