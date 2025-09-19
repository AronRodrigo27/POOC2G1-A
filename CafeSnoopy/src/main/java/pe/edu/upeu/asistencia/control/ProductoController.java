package pe.edu.upeu.asistencia.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.asistencia.modelo.Producto;
import pe.edu.upeu.asistencia.servicio.ProductoServicioI;

@Controller
public class ProductoController {

    @FXML private TextField txtCodigo, txtNombre, txtDescripcion, txtPrecio, txtStock;
    @FXML private CheckBox chkEstado;
    @FXML private TableView<Producto> tableProductos;

    private ObservableList<Producto> productos;

    @Autowired
    private ProductoServicioI productoServicio;

    private TableColumn<Producto, String> codigoCol, nombreCol, descripcionCol, precioCol, stockCol, estadoCol;
    private TableColumn<Producto, Void> opcCol;
    private int indexEdit = -1;

    @FXML
    public void initialize() {
        definirColumnas();
        listarProductos();
    }

    @FXML
    public void guardarProducto() {
        if (validarCampos()) {
            try {
                Producto producto = new Producto();
                producto.setCodigo(new SimpleStringProperty(txtCodigo.getText()));
                producto.setNombre(new SimpleStringProperty(txtNombre.getText()));
                producto.setDescripcion(new SimpleStringProperty(txtDescripcion.getText()));
                producto.setPrecio(new javafx.beans.property.SimpleDoubleProperty(Double.parseDouble(txtPrecio.getText())));
                producto.setStock(new javafx.beans.property.SimpleIntegerProperty(Integer.parseInt(txtStock.getText())));
                producto.setEstado(new javafx.beans.property.SimpleBooleanProperty(chkEstado.isSelected()));

                if (indexEdit == -1) {
                    productoServicio.save(producto);
                } else {
                    productoServicio.update(producto, indexEdit);
                    indexEdit = -1;
                }

                limpiarFormulario();
                listarProductos();
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "Precio y Stock deben ser valores numéricos válidos");
            }
        }
    }

    private boolean validarCampos() {
        if (txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty() ||
                txtPrecio.getText().isEmpty() || txtStock.getText().isEmpty()) {
            mostrarAlerta("Error", "Por favor complete todos los campos obligatorios");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        chkEstado.setSelected(true);
    }

    public void definirColumnas() {
        codigoCol = new TableColumn<>("Código");
        nombreCol = new TableColumn<>("Nombre");
        descripcionCol = new TableColumn<>("Descripción");
        precioCol = new TableColumn<>("Precio");
        stockCol = new TableColumn<>("Stock");
        estadoCol = new TableColumn<>("Estado");
        opcCol = new TableColumn<>("Opciones");
        opcCol.setPrefWidth(150);

        tableProductos.getColumns().addAll(codigoCol, nombreCol, descripcionCol, precioCol, stockCol, estadoCol, opcCol);
    }

    public void listarProductos() {
        codigoCol.setCellValueFactory(cellData -> cellData.getValue().getCodigo());
        nombreCol.setCellValueFactory(cellData -> cellData.getValue().getNombre());
        descripcionCol.setCellValueFactory(cellData -> cellData.getValue().getDescripcion());
        precioCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("S/. %.2f", cellData.getValue().getPrecio().get()))
        );
        stockCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getStock().get()))
        );
        estadoCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstado().get() ? "Activo" : "Inactivo")
        );

        agregarAccionesButton();
        productos = FXCollections.observableList(productoServicio.findAll());
        tableProductos.setItems(productos);
    }

    public void eliminarProducto(int index) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro de eliminar este producto?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                productoServicio.delete(index);
                listarProductos();
            }
        });
    }

    public void editarProducto(Producto producto, int index) {
        txtCodigo.setText(producto.getCodigo().get());
        txtNombre.setText(producto.getNombre().get());
        txtDescripcion.setText(producto.getDescripcion().get());
        txtPrecio.setText(String.valueOf(producto.getPrecio().get()));
        txtStock.setText(String.valueOf(producto.getStock().get()));
        chkEstado.setSelected(producto.getEstado().get());
        indexEdit = index;
    }

    public void agregarAccionesButton() {
        Callback<TableColumn<Producto, Void>, TableCell<Producto, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnEdit = new Button("Editar");
            private final Button btnDelete = new Button("Eliminar");

            {
                btnEdit.setOnAction(event -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    editarProducto(producto, getIndex());
                });

                btnDelete.setOnAction(event -> {
                    eliminarProducto(getIndex());
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
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
        opcCol.setCellFactory(cellFactory);
    }

    @FXML
    public void cancelarEdicion() {
        limpiarFormulario();
        indexEdit = -1;
    }
}