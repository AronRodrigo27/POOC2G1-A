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
import pe.edu.upeu.CafeSnoopy.modelo.Usuario;
import pe.edu.upeu.CafeSnoopy.servicio.UsuarioServicioI;

@Controller
public class UsuarioController {

    @FXML private TextField txtUsername, txtNombres, txtApellidos, txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private CheckBox chkEstado;
    @FXML private TableView<Usuario> tableUsuarios;

    private ObservableList<Usuario> usuarios;
    private int indexEdit = -1;

    @Autowired
    private UsuarioServicioI usuarioServicio;

    // Columnas de la tabla
    private TableColumn<Usuario, String> colUsername, colNombres, colApellidos, colEmail, colEstado;
    private TableColumn<Usuario, Void> colAcciones;

    @FXML
    public void initialize() {
        configurarTabla();
        listarUsuarios();
    }

    private void configurarTabla() {
        // Configurar columnas
        colUsername = new TableColumn<>("👤 Username");
        colNombres = new TableColumn<>("📛 Nombres");
        colApellidos = new TableColumn<>("📛 Apellidos");
        colEmail = new TableColumn<>("📧 Email");
        colEstado = new TableColumn<>("✅ Estado");
        colAcciones = new TableColumn<>("⚙️ Acciones");

        // Anchos preferidos
        colUsername.setPrefWidth(120);
        colNombres.setPrefWidth(150);
        colApellidos.setPrefWidth(150);
        colEmail.setPrefWidth(180);
        colEstado.setPrefWidth(80);
        colAcciones.setPrefWidth(120);

        tableUsuarios.getColumns().addAll(colUsername, colNombres, colApellidos, colEmail, colEstado, colAcciones);
    }

    @FXML
    public void guardarUsuario() {
        if (validarCampos()) {
            Usuario usuario = new Usuario();
            usuario.setUsername(new SimpleStringProperty(txtUsername.getText().trim()));
            usuario.setPassword(new SimpleStringProperty(txtPassword.getText()));
            usuario.setNombres(new SimpleStringProperty(txtNombres.getText().trim()));
            usuario.setApellidos(new SimpleStringProperty(txtApellidos.getText().trim()));
            usuario.setEmail(new SimpleStringProperty(txtEmail.getText().trim()));
            usuario.setEstado(new javafx.beans.property.SimpleBooleanProperty(chkEstado.isSelected()));

            try {
                if (indexEdit == -1) {
                    usuarioServicio.save(usuario);
                    mostrarAlerta("Éxito", "Usuario registrado correctamente", Alert.AlertType.INFORMATION);
                } else {
                    usuarioServicio.update(usuario, indexEdit);
                    mostrarAlerta("Éxito", "Usuario actualizado correctamente", Alert.AlertType.INFORMATION);
                    indexEdit = -1;
                }
                limpiarFormulario();
                listarUsuarios();
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al guardar el usuario: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private boolean validarCampos() {
        if (txtUsername.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "El username es obligatorio", Alert.AlertType.WARNING);
            txtUsername.requestFocus();
            return false;
        }
        if (txtPassword.getText().isEmpty()) {
            mostrarAlerta("Validación", "La contraseña es obligatoria", Alert.AlertType.WARNING);
            txtPassword.requestFocus();
            return false;
        }
        if (txtNombres.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "Los nombres son obligatorios", Alert.AlertType.WARNING);
            txtNombres.requestFocus();
            return false;
        }
        if (txtApellidos.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "Los apellidos son obligatorios", Alert.AlertType.WARNING);
            txtApellidos.requestFocus();
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
        txtUsername.clear();
        txtPassword.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtEmail.clear();
        chkEstado.setSelected(true);
        indexEdit = -1;
        txtUsername.requestFocus();
    }

    @FXML
    public void cancelarEdicion() {
        limpiarFormulario();
    }

    public void listarUsuarios() {
        // Configurar cell value factories
        colUsername.setCellValueFactory(cellData -> cellData.getValue().getUsername());
        colNombres.setCellValueFactory(cellData -> cellData.getValue().getNombres());
        colApellidos.setCellValueFactory(cellData -> cellData.getValue().getApellidos());
        colEmail.setCellValueFactory(cellData -> cellData.getValue().getEmail());
        colEstado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstado().get() ? "✅ Activo" : "❌ Inactivo")
        );

        // Agregar botones de acciones
        agregarBotonesAcciones();

        // Cargar datos
        usuarios = FXCollections.observableList(usuarioServicio.findAll());
        tableUsuarios.setItems(usuarios);
    }

    private void agregarBotonesAcciones() {
        Callback<TableColumn<Usuario, Void>, TableCell<Usuario, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Usuario, Void> call(final TableColumn<Usuario, Void> param) {
                return new TableCell<>() {
                    private final Button btnEdit = new Button("✏️");
                    private final Button btnDelete = new Button("🗑️");

                    {
                        btnEdit.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10;");
                        btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10;");

                        btnEdit.setOnAction(event -> {
                            Usuario usuario = getTableView().getItems().get(getIndex());
                            editarUsuario(usuario, getIndex());
                        });

                        btnDelete.setOnAction(event -> {
                            eliminarUsuario(getIndex());
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

    private void editarUsuario(Usuario usuario, int index) {
        txtUsername.setText(usuario.getUsername().get());
        txtPassword.setText(usuario.getPassword().get());
        txtNombres.setText(usuario.getNombres().get());
        txtApellidos.setText(usuario.getApellidos().get());
        txtEmail.setText(usuario.getEmail().get());
        chkEstado.setSelected(usuario.getEstado().get());
        indexEdit = index;

        // Scroll al formulario
        txtUsername.requestFocus();
    }

    private void eliminarUsuario(int index) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro de eliminar este usuario?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    usuarioServicio.delete(index);
                    mostrarAlerta("Éxito", "Usuario eliminado correctamente", Alert.AlertType.INFORMATION);
                    listarUsuarios();
                } catch (Exception e) {
                    mostrarAlerta("Error", "Error al eliminar el usuario: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }
}