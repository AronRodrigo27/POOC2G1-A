package pe.edu.upeu.CafeSnoopy.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.CafeSnoopy.modelo.*;
import pe.edu.upeu.CafeSnoopy.repositorio.*;
import pe.edu.upeu.CafeSnoopy.servicio.ApiService; // ✅ Servicio de API
import pe.edu.upeu.CafeSnoopy.servicio.TicketService;
import pe.edu.upeu.CafeSnoopy.utils.SesionGlobal; // ✅ Sesión Global

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VentasController {

    // --- Componentes FXML ---
    @FXML private TextField txtBuscarProducto, txtCliente;
    @FXML private TableView<Producto> tableProductos;
    @FXML private TableView<DetalleVenta> tableCarrito;
    @FXML private Label lblSubtotal, lblIgv, lblTotal;
    @FXML private TabPane tabPaneVentas;

    // --- Historial y Métricas ---
    @FXML private TableView<Venta> tableHistorial;
    @FXML private Label lblHistorialTotalVentas, lblHistorialIngresos;
    @FXML private Label lblMetricasVentaHoy, lblMetricasProdVendidos, lblMetricasClientes;
    @FXML private TableView<Object[]> tableTopProductos;

    // --- Inyección de Dependencias ---
    @Autowired private ProductoRepository productoRepo;
    @Autowired private VentaRepository ventaRepo;
    @Autowired private DetalleVentaRepository detalleRepo;
    @Autowired private TicketService ticketService;
    @Autowired private ApiService apiService; // ✅ Inyección del servicio de API

    // --- Listas Observables ---
    private ObservableList<Producto> productosDisponibles = FXCollections.observableArrayList();
    private ObservableList<DetalleVenta> carritoCompras = FXCollections.observableArrayList();
    private ObservableList<Venta> listaHistorial = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarTablaProductos();
        configurarTablaCarrito();
        configurarTablaHistorial();

        cargarProductos("");
        cargarHistorial();
        cargarMetricas();
    }

    // ==================== API RENIEC (NUEVO) ====================

    @FXML
    public void buscarClientePorDni() {
        String dni = txtCliente.getText().trim();

        // Validar formato DNI
        if (dni.length() == 8 && dni.matches("\\d+")) {
            // Llamar al servicio (puede tardar un poco, idealmente usar hilos en futuro)
            String nombreEncontrado = apiService.buscarPersona(dni);

            if (nombreEncontrado != null) {
                txtCliente.setText(nombreEncontrado); // Reemplaza DNI con Nombre
                mostrarAlerta("Éxito", "Cliente encontrado: " + nombreEncontrado, Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("No encontrado", "No se encontraron datos para el DNI: " + dni, Alert.AlertType.WARNING);
            }
        } else {
            mostrarAlerta("Error", "Ingrese un DNI válido de 8 dígitos.", Alert.AlertType.ERROR);
        }
    }

    // ==================== PROCESO DE VENTA ====================

    @FXML
    public void procesarVenta() {
        if(carritoCompras.isEmpty()) {
            mostrarAlerta("Error", "El carrito de compras está vacío.", Alert.AlertType.WARNING);
            return;
        }

        // ✅ Validación de Sesión
        if (SesionGlobal.usuarioLogueado == null) {
            mostrarAlerta("Error de Sesión", "No se ha identificado al usuario vendedor. Por favor inicie sesión nuevamente.", Alert.AlertType.ERROR);
            return;
        }

        try {
            Venta v = new Venta();
            v.setFechaVenta(LocalDateTime.now());

            // ✅ Asignar Vendedor Logueado
            v.setUsuario(SesionGlobal.usuarioLogueado);

            // Nombre del cliente (o General)
            if(txtCliente != null) v.setNombreCliente(txtCliente.getText().isEmpty() ? "Cliente General" : txtCliente.getText());

            // Calcular totales y asignar detalles
            BigDecimal total = BigDecimal.ZERO;
            for(DetalleVenta d : carritoCompras) {
                total = total.add(d.getSubtotal());
                d.setVenta(v);
            }
            v.setTotalVenta(total);
            v.setDetalles(new ArrayList<>(carritoCompras));

            // Guardar en Base de Datos
            ventaRepo.save(v);

            // Actualizar Stock
            for(DetalleVenta d : carritoCompras) {
                Producto p = d.getProducto();
                p.setStock(p.getStock() - d.getCantidad());
                productoRepo.save(p);
            }

            // Imprimir Ticket (Opcional)
            try {
                ticketService.imprimirVenta(v);
            } catch (Exception ex) {
                System.err.println("No se pudo imprimir el ticket: " + ex.getMessage());
            }

            mostrarAlerta("Éxito", "Venta registrada correctamente. ID: " + v.getIdVenta(), Alert.AlertType.INFORMATION);

            // Limpiar todo
            limpiarCarrito();
            if(txtCliente != null) txtCliente.clear();
            cargarProductos("");
            cargarHistorial();
            cargarMetricas();

        } catch(Exception e) {
            mostrarAlerta("Error Crítico", "Fallo al procesar la venta: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // ==================== GESTIÓN DE CARRITO ====================

    private void agregarAlCarrito(Producto p) {
        if(p.getStock() <= 0) { mostrarAlerta("Stock", "Producto agotado.", Alert.AlertType.WARNING); return; }

        for(DetalleVenta d : carritoCompras) {
            if(d.getProducto().getCodigo().equals(p.getCodigo())) {
                mostrarAlerta("Carrito", "El producto ya está en el carrito.", Alert.AlertType.INFORMATION);
                return;
            }
        }

        DetalleVenta d = new DetalleVenta();
        d.setProducto(p);
        d.setPrecioUnitario(p.getPrecio());
        d.setCantidad(1);
        d.calcularSubtotal();
        carritoCompras.add(d);
        calcularTotales();
    }

    private void calcularTotales() {
        BigDecimal total = BigDecimal.ZERO;
        for(DetalleVenta d : carritoCompras) total = total.add(d.getSubtotal());

        BigDecimal igv = total.multiply(new BigDecimal("0.18")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal subtotal = total.subtract(igv);

        if(lblTotal != null) lblTotal.setText("S/ " + total.setScale(2, RoundingMode.HALF_UP));
        if(lblSubtotal != null) lblSubtotal.setText("S/ " + subtotal);
        if(lblIgv != null) lblIgv.setText("S/ " + igv);
    }

    @FXML public void limpiarCarrito() {
        carritoCompras.clear();
        calcularTotales();
    }

    @FXML public void buscarProducto() {
        cargarProductos(txtBuscarProducto.getText());
    }

    // ==================== CONFIGURACIÓN DE TABLAS ====================

    private void configurarTablaProductos() {
        TableColumn<Producto, String> colNom = new TableColumn<>("Producto");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Producto, String> colPrec = new TableColumn<>("Precio");
        colPrec.setCellValueFactory(c -> new SimpleStringProperty("S/ " + c.getValue().getPrecio()));

        TableColumn<Producto, String> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<Producto, Void> colAccion = new TableColumn<>("Acción");
        colAccion.setCellFactory(p -> new TableCell<>() {
            private final Button btn = new Button("➕");
            {
                btn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
                btn.setOnAction(e -> agregarAlCarrito(getTableView().getItems().get(getIndex())));
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tableProductos.getColumns().setAll(colNom, colPrec, colStock, colAccion);
        tableProductos.setItems(productosDisponibles);
    }

    private void configurarTablaCarrito() {
        TableColumn<DetalleVenta, String> colProd = new TableColumn<>("Producto");
        colProd.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProducto().getNombre()));

        TableColumn<DetalleVenta, Void> colCant = new TableColumn<>("Cant.");
        colCant.setCellFactory(p -> new TableCell<>() {
            private final Button btnMenos = new Button("-");
            private final Label lblCant = new Label();
            private final Button btnMas = new Button("+");
            private final HBox pane = new HBox(5, btnMenos, lblCant, btnMas);
            {
                btnMenos.setOnAction(e -> {
                    DetalleVenta dt = getTableView().getItems().get(getIndex());
                    if(dt.getCantidad() > 1) {
                        dt.setCantidad(dt.getCantidad() - 1);
                        dt.calcularSubtotal();
                        tableCarrito.refresh();
                        calcularTotales();
                    }
                });
                btnMas.setOnAction(e -> {
                    DetalleVenta dt = getTableView().getItems().get(getIndex());
                    if(dt.getCantidad() < dt.getProducto().getStock()) {
                        dt.setCantidad(dt.getCantidad() + 1);
                        dt.calcularSubtotal();
                        tableCarrito.refresh();
                        calcularTotales();
                    }
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    lblCant.setText(String.valueOf(getTableView().getItems().get(getIndex()).getCantidad()));
                    setGraphic(pane);
                } else { setGraphic(null); }
            }
        });

        TableColumn<DetalleVenta, String> colSub = new TableColumn<>("Subtotal");
        colSub.setCellValueFactory(c -> new SimpleStringProperty("S/ " + c.getValue().getSubtotal()));

        TableColumn<DetalleVenta, Void> colDel = new TableColumn<>("X");
        colDel.setCellFactory(p -> new TableCell<>() {
            private final Button btn = new Button("🗑");
            {
                btn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");
                btn.setOnAction(e -> {
                    carritoCompras.remove(getIndex());
                    calcularTotales();
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tableCarrito.getColumns().setAll(colProd, colCant, colSub, colDel);
        tableCarrito.setItems(carritoCompras);
    }

    private void configurarTablaHistorial() {
        TableColumn<Venta, String> colId = new TableColumn<>("N°");
        colId.setCellValueFactory(new PropertyValueFactory<>("idVenta"));

        TableColumn<Venta, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

        TableColumn<Venta, String> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(c -> new SimpleStringProperty("S/ " + c.getValue().getTotalVenta()));

        tableHistorial.getColumns().setAll(colId, colFecha, colTotal);
        tableHistorial.setItems(listaHistorial);
    }

    // ==================== CARGA DE DATOS ====================

    private void cargarProductos(String filtro) {
        productosDisponibles.clear();
        if(filtro == null || filtro.isEmpty()) productosDisponibles.addAll(productoRepo.findAll());
        else productosDisponibles.addAll(productoRepo.buscarPorNombreOCodigo(filtro));
    }

    private void cargarHistorial() {
        listaHistorial.setAll(ventaRepo.findAll());
        if(lblHistorialTotalVentas != null) lblHistorialTotalVentas.setText(String.valueOf(listaHistorial.size()));
        double suma = listaHistorial.stream().mapToDouble(v -> v.getTotalVenta().doubleValue()).sum();
        if(lblHistorialIngresos != null) lblHistorialIngresos.setText(String.format("S/ %.2f", suma));
    }

    private void cargarMetricas() {
        try {
            if(lblMetricasVentaHoy != null) {
                BigDecimal totalHoy = ventaRepo.sumaVentasHoy();
                lblMetricasVentaHoy.setText("S/ " + (totalHoy != null ? totalHoy : "0.00"));
            }
            if(lblMetricasProdVendidos != null) {
                Integer prods = detalleRepo.contarProductosVendidosHoy();
                lblMetricasProdVendidos.setText(String.valueOf(prods != null ? prods : 0));
            }
            if(lblMetricasClientes != null) {
                Integer clientes = ventaRepo.contarVentasHoy();
                lblMetricasClientes.setText(String.valueOf(clientes != null ? clientes : 0));
            }
        } catch(Exception e) { e.printStackTrace(); }
    }

    private void mostrarAlerta(String t, String m, Alert.AlertType type) {
        Alert a = new Alert(type); a.setTitle(t); a.setHeaderText(null); a.setContentText(m); a.showAndWait();
    }

    @FXML public void irAReportes() {
        mostrarAlerta("Navegación", "Por favor, use el menú lateral para ir a Reportes.", Alert.AlertType.INFORMATION);
    }
}