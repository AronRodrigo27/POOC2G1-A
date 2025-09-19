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
import pe.edu.upeu.asistencia.modelo.Usuario;
import pe.edu.upeu.asistencia.servicio.UsuarioServicioI;

@Controller
public class UsuarioController {

    @FXML private TextField txtUsername, txtPassword, txtNombres, txtApellidos, txtEmail;
    @FXML private CheckBox chkEstado;
    @FXML private TableView<Usuario> tableUsuarios;

    private ObservableList<Usuario> usuarios;

    @Autowired
    private UsuarioServicioI usuarioServicio;

    private TableColumn<Usuario, String> usernameCol, nombresCol, apellidosCol, emailCol, estadoCol;
    private TableColumn<Usuario, Void> opcCol;
    private int indexEdit = -1;

    @FXML
    public void initialize() {
        definirColumnas();
        listarUsuarios();
    }

    @FXML
    public void guardarUsuario() {
        if (validarCampos()) {
            Usuario usuario = new Usuario();
            usuario.setUsername(new SimpleStringProperty(txtUsername.getText()));
            usuario.setPassword(new SimpleStringProperty(txtPassword.getText()));
            usuario.setNombres(new SimpleStringProperty(txtNombres.getText()));
            usuario.setApellidos(new SimpleStringProperty(txtApellidos.getText()));
            usuario.setEmail(new SimpleStringProperty(txtEmail.getText()));
            usuario.setEstado(new javafx.beans.property.SimpleBooleanProperty(chkEstado.isSelected()));

            if (indexEdit == -1) {
                usuarioServicio.save(usuario);
            } else {
                usuarioServicio.update(usuario, indexEdit);
                indexEdit = -1;
            }

            limpiarFormulario();
            listarUsuarios();
        }
    }

    private boolean validarCampos() {
        if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty() ||
                txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty()) {
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
        txtUsername.setText("");
        txtPassword.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtEmail.setText("");
        chkEstado.setSelected(true);
    }

    public void definirColumnas() {
        usernameCol = new TableColumn<>("Username");
        nombresCol = new TableColumn<>("Nombres");
        apellidosCol = new TableColumn<>("Apellidos");
        emailCol = new TableColumn<>("Email");
        estadoCol = new TableColumn<>("Estado");
        opcCol = new TableColumn<>("Opciones");
        opcCol.setPrefWidth(150);

        tableUsuarios.getColumns().addAll(usernameCol, nombresCol, apellidosCol, emailCol, estadoCol, opcCol);
    }

    public void listarUsuarios() {
        usernameCol.setCellValueFactory(cellData -> cellData.getValue().getUsername());
        nombresCol.setCellValueFactory(cellData -> cellData.getValue().getNombres());
        apellidosCol.setCellValueFactory(cellData -> cellData.getValue().getApellidos());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().getEmail());
        estadoCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstado().get() ? "Activo" : "Inactivo")
        );

        agregarAccionesButton();
        usuarios = FXCollections.observableList(usuarioServicio.findAll());
        tableUsuarios.setItems(usuarios);
    }

    public void eliminarUsuario(int index) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro de eliminar este usuario?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                usuarioServicio.delete(index);
                listarUsuarios();
            }
        });
    }

    public void editarUsuario(Usuario usuario, int index) {
        txtUsername.setText(usuario.getUsername().get());
        txtPassword.setText(usuario.getPassword().get());
        txtNombres.setText(usuario.getNombres().get());
        txtApellidos.setText(usuario.getApellidos().get());
        txtEmail.setText(usuario.getEmail().get());
        chkEstado.setSelected(usuario.getEstado().get());
        indexEdit = index;
    }

    public void agregarAccionesButton() {
        Callback<TableColumn<Usuario, Void>, TableCell<Usuario, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnEdit = new Button("Editar");
            private final Button btnDelete = new Button("Eliminar");

            {
                btnEdit.setOnAction(event -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    editarUsuario(usuario, getIndex());
                });

                btnDelete.setOnAction(event -> {
                    eliminarUsuario(getIndex());
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