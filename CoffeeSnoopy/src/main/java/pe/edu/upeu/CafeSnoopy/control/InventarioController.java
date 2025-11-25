package pe.edu.upeu.CafeSnoopy.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.CafeSnoopy.modelo.Categoria;
import pe.edu.upeu.CafeSnoopy.modelo.MovimientoInventario;
import pe.edu.upeu.CafeSnoopy.modelo.Producto;
import pe.edu.upeu.CafeSnoopy.repositorio.CategoriaRepository;
import pe.edu.upeu.CafeSnoopy.repositorio.MovimientoInventarioRepository;
import pe.edu.upeu.CafeSnoopy.repositorio.ProductoRepository;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class InventarioController {

    // --- Pestaña 1: Gestión ---
    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> comboCategoria, comboEstado;
    @FXML private TableView<Producto> tablaInventario;

    // Tarjetas Resumen (Asegúrate de que tengan fx:id en el FXML)
    @FXML private Label lblTotalProductos, lblStockBajo, lblSinStock, lblValorTotal;

    // --- Pestaña 2: Gráficos ---
    @FXML private BarChart<String, Number> barChartCategorias;
    @FXML private PieChart pieChartValor;

    // --- Pestaña 3: Movimientos ---
    @FXML private TableView<MovimientoInventario> tablaMovimientos;
    @FXML private Label lblEntradasHoy, lblSalidasHoy, lblAjustesHoy;

    @Autowired private ProductoRepository productoRepo;
    @Autowired private CategoriaRepository categoriaRepo;
    @Autowired private MovimientoInventarioRepository movimientoRepo;

    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private ObservableList<MovimientoInventario> listaMovimientos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        inicializarCombos();
        configurarTablas();
        cargarDatos();
        cargarEstadisticas();
        cargarGraficos();
    }

    private void inicializarCombos() {
        comboCategoria.getItems().add("Todas");
        List<Categoria> categorias = categoriaRepo.findAll();
        for(Categoria c : categorias) comboCategoria.getItems().add(c.getNombreCategoria());
        comboCategoria.getSelectionModel().selectFirst();

        comboEstado.getItems().addAll("Todos", "Stock Normal", "Stock Bajo", "Sin Stock");
        comboEstado.getSelectionModel().selectFirst();
    }

    private void configurarTablas() {
        // Tabla Inventario
        TableColumn<Producto, String> colCod = new TableColumn<>("Código");
        colCod.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        TableColumn<Producto, String> colNom = new TableColumn<>("Producto");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<Producto, String> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty("S/ " + cell.getValue().getPrecio()));

        tablaInventario.getColumns().setAll(colCod, colNom, colStock, colPrecio);
        tablaInventario.setItems(listaProductos);

        // Tabla Movimientos
        if(tablaMovimientos != null) {
            TableColumn<MovimientoInventario, String> colFecha = new TableColumn<>("Fecha");
            colFecha.setCellValueFactory(cell ->
                    new javafx.beans.property.SimpleStringProperty(
                            cell.getValue().getFechaMovimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

            TableColumn<MovimientoInventario, String> colTipo = new TableColumn<>("Tipo");
            colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoMovimiento"));

            TableColumn<MovimientoInventario, String> colProdMov = new TableColumn<>("Producto");
            colProdMov.setCellValueFactory(cell ->
                    new javafx.beans.property.SimpleStringProperty(cell.getValue().getProducto().getNombre()));

            TableColumn<MovimientoInventario, Integer> colCant = new TableColumn<>("Cant.");
            colCant.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

            tablaMovimientos.getColumns().setAll(colFecha, colTipo, colProdMov, colCant);
            tablaMovimientos.setItems(listaMovimientos);
        }
    }

    private void cargarDatos() {
        listaProductos.setAll(productoRepo.findAll());
        listaMovimientos.setAll(movimientoRepo.findAll());
    }

    private void cargarEstadisticas() {
        // Pestaña 1
        if(lblTotalProductos != null) lblTotalProductos.setText(String.valueOf(productoRepo.count()));
        if(lblStockBajo != null) lblStockBajo.setText(String.valueOf(productoRepo.countByStockLessThan(5)));
        if(lblSinStock != null) lblSinStock.setText(String.valueOf(productoRepo.countByStockLessThan(1)));

        BigDecimal valor = productoRepo.obtenerValorTotalInventario();
        if(lblValorTotal != null) lblValorTotal.setText("S/ " + (valor != null ? valor : "0.00"));

        // Pestaña 3
        try {
            if(lblEntradasHoy != null)
                lblEntradasHoy.setText(String.valueOf(movimientoRepo.sumarCantidadPorTipoHoy("ENTRADA")));
            if(lblSalidasHoy != null)
                lblSalidasHoy.setText(String.valueOf(movimientoRepo.sumarCantidadPorTipoHoy("SALIDA"))); // Ventas
            if(lblAjustesHoy != null)
                lblAjustesHoy.setText(String.valueOf(movimientoRepo.sumarCantidadPorTipoHoy("AJUSTE")));
        } catch (Exception e) {
            // Manejo si devuelve null
        }
    }

    private void cargarGraficos() {
        // Gráfico de Stock por Categoría
        if(barChartCategorias != null) {
            barChartCategorias.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Stock");

            Map<String, Integer> stockPorCat = listaProductos.stream()
                    .collect(Collectors.groupingBy(
                            p -> p.getCategoria() != null ? p.getCategoria().getNombreCategoria() : "Sin Categoría",
                            Collectors.summingInt(Producto::getStock)
                    ));

            stockPorCat.forEach((cat, stock) -> series.getData().add(new XYChart.Data<>(cat, stock)));
            barChartCategorias.getData().add(series);
        }

        // Gráfico de Valor (Pastel)
        if(pieChartValor != null) {
            pieChartValor.getData().clear();
            // Simplificación: Valor por categoría
            Map<String, Double> valorPorCat = listaProductos.stream()
                    .collect(Collectors.groupingBy(
                            p -> p.getCategoria() != null ? p.getCategoria().getNombreCategoria() : "Sin Categoría",
                            Collectors.summingDouble(p -> p.getPrecio().doubleValue() * p.getStock())
                    ));

            valorPorCat.forEach((cat, valor) ->
                    pieChartValor.getData().add(new PieChart.Data(cat, valor)));
        }
    }

    @FXML
    public void buscarProducto() {
        // Implementar filtro simple en la lista observable
        String filtro = txtBuscar.getText().toLowerCase();
        if(filtro.isEmpty()) {
            tablaInventario.setItems(listaProductos);
        } else {
            ObservableList<Producto> filtrados = listaProductos.filtered(p ->
                    p.getNombre().toLowerCase().contains(filtro) ||
                            p.getCodigo().toLowerCase().contains(filtro));
            tablaInventario.setItems(filtrados);
        }
    }
}