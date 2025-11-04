package pe.edu.upeu.CafeSnoopy.control;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import org.springframework.stereotype.Controller;

@Controller
public class InventarioController {

    @FXML
    private TextField txtBuscar;

    @FXML
    private ComboBox<String> comboCategoria;

    @FXML
    private ComboBox<String> comboEstado;

    @FXML
    private TableView<?> tablaInventario;

    @FXML
    private BarChart<String, Number> barChartCategorias;

    @FXML
    private PieChart pieChartValor;

    @FXML
    public void initialize() {
        // Inicializar ComboBoxes
        inicializarCombos();

        // Inicializar gráficos
        inicializarGraficos();
    }

    private void inicializarCombos() {
        // Categorías
        comboCategoria.getItems().addAll(
                "Todas las Categorías",
                "Bebidas Calientes",
                "Bebidas Frías",
                "Sandwiches",
                "Postres",
                "Snacks",
                "Ingredientes"
        );

        // Estados de stock
        comboEstado.getItems().addAll(
                "Todos los Estados",
                "Stock Normal",
                "Stock Bajo",
                "Sin Stock",
                "Sobre Stock"
        );
    }

    private void inicializarGraficos() {
        // BarChart - Stock por categoría
        XYChart.Series<String, Number> seriesStock = new XYChart.Series<>();
        seriesStock.setName("Stock por Categoría");
        seriesStock.getData().add(new XYChart.Data<>("Beb. Calientes", 45));
        seriesStock.getData().add(new XYChart.Data<>("Beb. Frías", 28));
        seriesStock.getData().add(new XYChart.Data<>("Sandwiches", 35));
        seriesStock.getData().add(new XYChart.Data<>("Postres", 22));
        seriesStock.getData().add(new XYChart.Data<>("Snacks", 18));
        seriesStock.getData().add(new XYChart.Data<>("Ingredientes", 8));
        barChartCategorias.getData().add(seriesStock);

        // PieChart - Distribución de valor
        PieChart.Data slice1 = new PieChart.Data("Bebidas Calientes", 35);
        PieChart.Data slice2 = new PieChart.Data("Bebidas Frías", 25);
        PieChart.Data slice3 = new PieChart.Data("Sandwiches", 20);
        PieChart.Data slice4 = new PieChart.Data("Postres", 12);
        PieChart.Data slice5 = new PieChart.Data("Snacks", 5);
        PieChart.Data slice6 = new PieChart.Data("Ingredientes", 3);

        pieChartValor.getData().addAll(slice1, slice2, slice3, slice4, slice5, slice6);
    }

    // Métodos para manejar eventos (puedes agregarlos según necesites)
    @FXML
    private void handleBuscar() {
        // Lógica para buscar productos
    }

    @FXML
    private void handleNuevoProducto() {
        // Lógica para agregar nuevo producto
    }

    @FXML
    private void handleActualizarStock() {
        // Lógica para actualizar stock
    }
}