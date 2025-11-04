package pe.edu.upeu.CafeSnoopy.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.CafeSnoopy.modelo.Producto;
import pe.edu.upeu.CafeSnoopy.servicio.ProductoServicioI;

@Controller
public class ProductoController {

    @FXML private TextField txtCodigo, txtNombre, txtDescripcion, txtPrecio, txtStock;
    @FXML private CheckBox chkEstado;
    @FXML private TableView<Producto> tableProductos;

    private ObservableList<Producto> productos;
    private int indexEdit = -1;

    @Autowired
    private ProductoServicioI productoServicio;

    // Columnas de la tabla
    private TableColumn<Producto, String> colCodigo, colNombre, colDescripcion, colPrecio, colStock, colEstado;
    private TableColumn<Producto, Void> colAcciones;

    @FXML
    public void initialize() {
        configurarTabla();
        listarProductos();
    }

    private void configurarTabla() {
        // Configurar columnas
        colCodigo = new TableColumn<>("🔢 Código");
        colNombre = new TableColumn<>("🏷️ Nombre");
        colDescripcion = new TableColumn<>("📝 Descripción");
        colPrecio = new TableColumn<>("💰 Precio");
        colStock = new TableColumn<>("📊 Stock");
        colEstado = new TableColumn<>("✅ Estado");
        colAcciones = new TableColumn<>("⚙️ Acciones");

        // Anchos preferidos
        colCodigo.setPrefWidth(100);
        colNombre.setPrefWidth(150);
        colDescripcion.setPrefWidth(200);
        colPrecio.setPrefWidth(100);
        colStock.setPrefWidth(80);
        colEstado.setPrefWidth(80);
        colAcciones.setPrefWidth(120);

        tableProductos.getColumns().addAll(colCodigo, colNombre, colDescripcion, colPrecio, colStock, colEstado, colAcciones);
    }

    @FXML
    public void guardarProducto() {
        if (validarCampos()) {
            try {
                Producto producto = new Producto();
                producto.setCodigo(new SimpleStringProperty(txtCodigo.getText().trim()));
                producto.setNombre(new SimpleStringProperty(txtNombre.getText().trim()));
                producto.setDescripcion(new SimpleStringProperty(txtDescripcion.getText().trim()));
                producto.setPrecio(new javafx.beans.property.SimpleDoubleProperty(Double.parseDouble(txtPrecio.getText())));
                producto.setStock(new javafx.beans.property.SimpleIntegerProperty(Integer.parseInt(txtStock.getText())));
                producto.setEstado(new javafx.beans.property.SimpleBooleanProperty(chkEstado.isSelected()));

                if (indexEdit == -1) {
                    productoServicio.save(producto);
                    mostrarAlerta("Éxito", "Producto registrado correctamente", Alert.AlertType.INFORMATION);
                } else {
                    productoServicio.update(producto, indexEdit);
                    mostrarAlerta("Éxito", "Producto actualizado correctamente", Alert.AlertType.INFORMATION);
                    indexEdit = -1;
                }
                limpiarFormulario();
                listarProductos();
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "Precio y Stock deben ser valores numéricos válidos", Alert.AlertType.ERROR);
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al guardar el producto: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private boolean validarCampos() {
        if (txtCodigo.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "El código del producto es obligatorio", Alert.AlertType.WARNING);
            txtCodigo.requestFocus();
            return false;
        }
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "El nombre del producto es obligatorio", Alert.AlertType.WARNING);
            txtNombre.requestFocus();
            return false;
        }
        if (txtPrecio.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "El precio es obligatorio", Alert.AlertType.WARNING);
            txtPrecio.requestFocus();
            return false;
        }
        if (txtStock.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "El stock es obligatorio", Alert.AlertType.WARNING);
            txtStock.requestFocus();
            return false;
        }

        // Validar que precio sea numérico positivo
        try {
            double precio = Double.parseDouble(txtPrecio.getText());
            if (precio < 0) {
                mostrarAlerta("Validación", "El precio debe ser un valor positivo", Alert.AlertType.WARNING);
                txtPrecio.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Validación", "El precio debe ser un número válido", Alert.AlertType.WARNING);
            txtPrecio.requestFocus();
            return false;
        }

        // Validar que stock sea numérico entero positivo
        try {
            int stock = Integer.parseInt(txtStock.getText());
            if (stock < 0) {
                mostrarAlerta("Validación", "El stock debe ser un valor positivo", Alert.AlertType.WARNING);
                txtStock.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Validación", "El stock debe ser un número entero válido", Alert.AlertType.WARNING);
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

    @FXML
    public void limpiarFormulario() {
        txtCodigo.clear();
        txtNombre.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        txtStock.clear();
        chkEstado.setSelected(true);
        indexEdit = -1;
        txtCodigo.requestFocus();
    }

    @FXML
    public void cancelarEdicion() {
        limpiarFormulario();
    }

    public void listarProductos() {
        // Configurar cell value factories
        colCodigo.setCellValueFactory(cellData -> cellData.getValue().getCodigo());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().getNombre());
        colDescripcion.setCellValueFactory(cellData -> cellData.getValue().getDescripcion());
        colPrecio.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("S/ %.2f", cellData.getValue().getPrecio().get()))
        );
        colStock.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getStock().get()))
        );
        colEstado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstado().get() ? "✅ Activo" : "❌ Inactivo")
        );

        // Agregar botones de acciones
        agregarBotonesAcciones();

        // Cargar datos
        productos = FXCollections.observableList(productoServicio.findAll());
        tableProductos.setItems(productos);
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

                        btnEdit.setOnAction(event -> {
                            Producto producto = getTableView().getItems().get(getIndex());
                            editarProducto(producto, getIndex());
                        });

                        btnDelete.setOnAction(event -> {
                            eliminarProducto(getIndex());
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox(btnEdit, btnDelete);
                            hbox.setSpacing(5);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        };
        colAcciones.setCellFactory(cellFactory);
    }

    private void editarProducto(Producto producto, int index) {
        txtCodigo.setText(producto.getCodigo().get());
        txtNombre.setText(producto.getNombre().get());
        txtDescripcion.setText(producto.getDescripcion().get());
        txtPrecio.setText(String.valueOf(producto.getPrecio().get()));
        txtStock.setText(String.valueOf(producto.getStock().get()));
        chkEstado.setSelected(producto.getEstado().get());
        indexEdit = index;

        // Scroll al formulario
        txtCodigo.requestFocus();
    }

    private void eliminarProducto(int index) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro de eliminar este producto?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    productoServicio.delete(index);
                    mostrarAlerta("Éxito", "Producto eliminado correctamente", Alert.AlertType.INFORMATION);
                    listarProductos();
                } catch (Exception e) {
                    mostrarAlerta("Error", "Error al eliminar el producto: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }
}