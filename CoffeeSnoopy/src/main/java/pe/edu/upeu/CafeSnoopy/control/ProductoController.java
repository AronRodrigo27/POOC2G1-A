package pe.edu.upeu.CafeSnoopy.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.CafeSnoopy.modelo.Producto;
import pe.edu.upeu.CafeSnoopy.repositorio.ProductoRepository;
import pe.edu.upeu.CafeSnoopy.servicio.ProductoServicioI;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductoController {

    // --- Componentes del Formulario ---
    @FXML private TextField txtCodigo, txtNombre, txtDescripcion, txtPrecio, txtStock;
    @FXML private CheckBox chkEstado;
    @FXML private TableView<Producto> tableProductos;

    // --- Componentes de Estadísticas ---
    @FXML private Label lblTotalProductos;
    @FXML private Label lblValorTotal;
    @FXML private Label lblStockBajo;

    private ObservableList<Producto> productos;
    private String codigoEdit = null; // Usamos el Código (PK) para editar

    @Autowired
    private ProductoServicioI productoServicio;

    @Autowired
    private ProductoRepository productoRepo;

    // Columnas de la tabla
    private TableColumn<Producto, String> colCodigo, colNombre, colDescripcion, colPrecio, colStock, colEstado;
    private TableColumn<Producto, Void> colAcciones;

    @FXML
    public void initialize() {
        configurarTabla();
        listarProductos();
        actualizarEstadisticas();

        // ✅ NUEVO: Generar código automático al abrir la ventana
        limpiarFormulario();
    }

    private void configurarTabla() {
        colCodigo = new TableColumn<>("🔢 Código");
        colNombre = new TableColumn<>("🏷️ Nombre");
        colDescripcion = new TableColumn<>("📝 Descripción");
        colPrecio = new TableColumn<>("💰 Precio");
        colStock = new TableColumn<>("📊 Stock");
        colEstado = new TableColumn<>("✅ Estado");
        colAcciones = new TableColumn<>("⚙️ Acciones");

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        colPrecio.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(String.format("S/ %.2f", cellData.getValue().getPrecio()))
        );
        colStock.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getStock()))
        );
        colEstado.setCellValueFactory(cellData -> {
            boolean estado = cellData.getValue().getEstado();
            return new javafx.beans.property.SimpleStringProperty(estado ? "✅ Activo" : "❌ Inactivo");
        });

        colCodigo.setPrefWidth(100);
        colNombre.setPrefWidth(150);
        colDescripcion.setPrefWidth(200);
        colPrecio.setPrefWidth(100);
        colStock.setPrefWidth(80);
        colEstado.setPrefWidth(80);
        colAcciones.setPrefWidth(120);

        tableProductos.getColumns().setAll(colCodigo, colNombre, colDescripcion, colPrecio, colStock, colEstado, colAcciones);
    }

    @FXML
    public void guardarProducto() {
        if (validarCampos()) {
            try {
                Producto producto = new Producto();

                // 1. Asignar Código (PK) - Viene del campo autogenerado
                producto.setCodigo(txtCodigo.getText().trim());

                // 2. Si es edición, verificamos integridad
                if (codigoEdit != null) {
                    Optional<Producto> prodOpt = productoServicio.findById(codigoEdit);
                    if (prodOpt.isPresent()) {
                        producto.setCategoria(prodOpt.get().getCategoria());
                        // Aseguramos que el código sea el que estamos editando
                        producto.setCodigo(codigoEdit);
                    }
                }

                // 3. Asignar resto de campos
                producto.setNombre(txtNombre.getText().trim());
                producto.setDescripcion(txtDescripcion.getText().trim());
                producto.setPrecio(new BigDecimal(txtPrecio.getText()));
                producto.setStock(Integer.parseInt(txtStock.getText()));
                producto.setEstado(chkEstado.isSelected());

                if (codigoEdit == null) {
                    productoServicio.save(producto);
                    mostrarAlerta("Éxito", "Producto registrado correctamente con código: " + producto.getCodigo(), Alert.AlertType.INFORMATION);
                } else {
                    productoServicio.update(producto);
                    mostrarAlerta("Éxito", "Producto actualizado correctamente", Alert.AlertType.INFORMATION);
                }

                limpiarFormulario(); // Esto generará el siguiente código automáticamente
                listarProductos();
                actualizarEstadisticas();

            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "Precio y Stock deben ser valores numéricos válidos", Alert.AlertType.ERROR);
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al guardar el producto: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    // ✅ MÉTODO MODIFICADO: Autogenera el código
    @FXML
    public void limpiarFormulario() {
        // Limpiar campos de datos
        txtNombre.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        txtStock.clear();
        chkEstado.setSelected(true);

        codigoEdit = null; // Reseteamos bandera de edición

        // Configuración del campo Código
        txtCodigo.setDisable(true); // Bloqueado para que no escriban manualmente
        try {
            // Llamada al servicio para obtener el siguiente correlativo (PROD-00X)
            String nuevoCodigo = productoServicio.generarCodigo();
            txtCodigo.setText(nuevoCodigo);
        } catch (Exception e) {
            txtCodigo.setText("PROD-001"); // Fallback por si falla la BD
            System.err.println("Error generando código: " + e.getMessage());
        }

        // Poner el foco en el nombre para empezar a escribir directo
        txtNombre.requestFocus();
    }

    @FXML
    public void cancelarEdicion() {
        limpiarFormulario();
    }

    public void listarProductos() {
        List<Producto> listaJPA = productoServicio.findAll();
        productos = FXCollections.observableArrayList(listaJPA);
        tableProductos.setItems(productos);
        agregarBotonesAcciones();
    }

    private void actualizarEstadisticas() {
        try {
            if (lblTotalProductos != null) lblTotalProductos.setText(String.valueOf(productoRepo.count()));

            BigDecimal valor = productoRepo.obtenerValorTotalInventario();
            if (lblValorTotal != null) lblValorTotal.setText(String.format("S/ %.2f", valor != null ? valor : BigDecimal.ZERO));

            if (lblStockBajo != null) lblStockBajo.setText(String.valueOf(productoRepo.countByStockLessThan(5)));

        } catch (Exception e) {
            System.err.println("Error al actualizar estadísticas: " + e.getMessage());
        }
    }

    private void agregarBotonesAcciones() {
        Callback<TableColumn<Producto, Void>, TableCell<Producto, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Producto, Void> call(final TableColumn<Producto, Void> param) {
                return new TableCell<>() {
                    private final Button btnEdit = new Button("✏️");
                    private final Button btnDelete = new Button("🗑️");

                    {
                        btnEdit.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10;");
                        btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10;");

                        btnEdit.setOnAction(event -> editarProducto(getTableView().getItems().get(getIndex())));
                        btnDelete.setOnAction(event -> eliminarProducto(getTableView().getItems().get(getIndex())));
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox(5, btnEdit, btnDelete);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        };
        colAcciones.setCellFactory(cellFactory);
    }

    private void editarProducto(Producto producto) {
        // Llenar campos con datos existentes
        txtCodigo.setText(producto.getCodigo());
        txtNombre.setText(producto.getNombre());
        txtDescripcion.setText(producto.getDescripcion());
        txtPrecio.setText(String.valueOf(producto.getPrecio()));
        txtStock.setText(String.valueOf(producto.getStock()));
        chkEstado.setSelected(producto.getEstado());

        // Configurar modo edición
        codigoEdit = producto.getCodigo();
        txtCodigo.setDisable(true); // El código nunca se debe editar (es PK)
        txtNombre.requestFocus();
    }

    private void eliminarProducto(Producto producto) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro de eliminar " + producto.getNombre() + "?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    productoServicio.delete(producto.getCodigo());
                    mostrarAlerta("Éxito", "Producto eliminado", Alert.AlertType.INFORMATION);
                    listarProductos();
                    actualizarEstadisticas();
                    // Si eliminamos el último, regeneramos el código sugerido
                    limpiarFormulario();
                } catch (Exception e) {
                    mostrarAlerta("Error", "No se puede eliminar (puede tener ventas asociadas).", Alert.AlertType.ERROR);
                }
            }
        });
    }

    private boolean validarCampos() {
        // Validación básica
        if (txtCodigo.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "Error interno: Código no generado.", Alert.AlertType.ERROR);
            return false;
        }
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "El nombre es obligatorio", Alert.AlertType.WARNING);
            txtNombre.requestFocus();
            return false;
        }
        if (txtPrecio.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "El precio es obligatorio", Alert.AlertType.WARNING);
            txtPrecio.requestFocus();
            return false;
        }
        // Validaciones numéricas
        try {
            if (Double.parseDouble(txtPrecio.getText()) < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            mostrarAlerta("Validación", "El precio debe ser un número positivo", Alert.AlertType.WARNING);
            txtPrecio.requestFocus();
            return false;
        }
        try {
            if (Integer.parseInt(txtStock.getText()) < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            mostrarAlerta("Validación", "El stock debe ser un número entero positivo", Alert.AlertType.WARNING);
            txtStock.requestFocus();
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}