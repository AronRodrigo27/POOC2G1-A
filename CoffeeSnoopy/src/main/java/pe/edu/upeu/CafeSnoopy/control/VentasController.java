package pe.edu.upeu.CafeSnoopy.control;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Controller;

@Controller
public class VentasController {

    @FXML
    private TextField txtBuscarProducto;

    @FXML
    private TableView<?> tableProductos;

    @FXML
    private TableView<?> tableCarrito;

    @FXML
    public void initialize() {
        // Inicializar componentes de ventas
        configurarTablas();
    }

    private void configurarTablas() {
        // Configuración de tablas de productos y carrito
        // Aquí puedes agregar la lógica para cargar productos disponibles
    }

    // Métodos para manejar las acciones de ventas
    @FXML
    private void procesarVenta() {
        // Lógica para procesar la venta
        mostrarAlerta("Venta Procesada", "La venta se ha procesado correctamente", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void limpiarCarrito() {
        // Lógica para limpiar el carrito
        mostrarAlerta("Carrito Limpiado", "El carrito ha sido vaciado", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void agregarAlCarrito() {
        // Lógica para agregar producto al carrito
        mostrarAlerta("Producto Agregado", "Producto agregado al carrito", Alert.AlertType.INFORMATION);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}