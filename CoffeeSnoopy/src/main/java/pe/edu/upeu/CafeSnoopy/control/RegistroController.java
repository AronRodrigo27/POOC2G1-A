package pe.edu.upeu.CafeSnoopy.control;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.CafeSnoopy.modelo.Usuario;
import pe.edu.upeu.CafeSnoopy.servicio.UsuarioServicioI;

import java.util.Optional;

@Controller
public class RegistroController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEmail;

    @Autowired
    private UsuarioServicioI usuarioServicio;

    @FXML
    public void initialize() {
        // Configuración inicial
        txtUsername.requestFocus();
    }

    @FXML
    public void handleRegistro() {
        if (validarCampos()) {
            if (validarUsuarioExistente()) {
                registrarUsuario();
            }
        }
    }

    @FXML
    public void handleCancelar() {
        cerrarVentana();
    }

    private boolean validarCampos() {
        // Validar username
        if (txtUsername.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "El usuario es obligatorio", Alert.AlertType.WARNING);
            txtUsername.requestFocus();
            return false;
        }

        if (txtUsername.getText().trim().length() < 3) {
            mostrarAlerta("Validación", "El usuario debe tener al menos 3 caracteres", Alert.AlertType.WARNING);
            txtUsername.requestFocus();
            return false;
        }

        // Validar password
        if (txtPassword.getText().isEmpty()) {
            mostrarAlerta("Validación", "La contraseña es obligatoria", Alert.AlertType.WARNING);
            txtPassword.requestFocus();
            return false;
        }

        // Validar confirmación de password
        if (txtConfirmPassword.getText().isEmpty()) {
            mostrarAlerta("Validación", "Debe confirmar la contraseña", Alert.AlertType.WARNING);
            txtConfirmPassword.requestFocus();
            return false;
        }

        // Validar que las contraseñas coincidan
        if (!txtPassword.getText().equals(txtConfirmPassword.getText())) {
            mostrarAlerta("Validación", "Las contraseñas no coinciden", Alert.AlertType.WARNING);
            txtPassword.clear();
            txtConfirmPassword.clear();
            txtPassword.requestFocus();
            return false;
        }

        // Validar longitud mínima de password
        if (txtPassword.getText().length() < 4) {
            mostrarAlerta("Validación", "La contraseña debe tener al menos 4 caracteres", Alert.AlertType.WARNING);
            txtPassword.requestFocus();
            return false;
        }

        // Validar nombres
        if (txtNombres.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "Los nombres son obligatorios", Alert.AlertType.WARNING);
            txtNombres.requestFocus();
            return false;
        }

        // Validar apellidos
        if (txtApellidos.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "Los apellidos son obligatorios", Alert.AlertType.WARNING);
            txtApellidos.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validarUsuarioExistente() {
        String username = txtUsername.getText().trim();
        Optional<Usuario> usuarioExistente = usuarioServicio.findByUsername(username);

        if (usuarioExistente.isPresent()) {
            mostrarAlerta("Usuario Existente",
                    "El usuario '" + username + "' ya existe. Por favor elija otro nombre de usuario.",
                    Alert.AlertType.WARNING);
            txtUsername.clear();
            txtUsername.requestFocus();
            return false;
        }
        return true;
    }

    private void registrarUsuario() {
        try {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsername(txtUsername.getText().trim());
            nuevoUsuario.setPassword(txtPassword.getText());
            nuevoUsuario.setNombres(txtNombres.getText().trim());
            nuevoUsuario.setApellidos(txtApellidos.getText().trim());
            nuevoUsuario.setEmail(txtEmail.getText().trim());

            // MODIFICACIÓN: Crear usuarios activos por defecto para testing
            nuevoUsuario.setEstado(true); // Cambiado a true para que puedan loguearse
            nuevoUsuario.setRol("VENDEDOR");

            Usuario usuarioGuardado = usuarioServicio.save(nuevoUsuario);

            if (usuarioGuardado != null && usuarioGuardado.getIdUsuario() != null) {
                mostrarAlerta("Registro Exitoso",
                        "✅ Su cuenta ha sido creada exitosamente.\n\n" +
                                "📝 Datos de su cuenta:\n" +
                                "• Usuario: " + nuevoUsuario.getUsername() + "\n" +
                                "• Nombre: " + nuevoUsuario.getNombres() + " " + nuevoUsuario.getApellidos() + "\n" +
                                "• Rol: " + nuevoUsuario.getRol() + "\n" +
                                "• Estado: ACTIVO\n\n" +
                                "✅ Ya puede iniciar sesión con su nueva cuenta.",
                        Alert.AlertType.INFORMATION);

                cerrarVentana();
            } else {
                throw new Exception("No se pudo guardar el usuario en la base de datos");
            }

        } catch (Exception e) {
            mostrarAlerta("Error",
                    "❌ Error al crear la cuenta: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtUsername.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}