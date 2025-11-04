package pe.edu.upeu.CafeSnoopy.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.CafeSnoopy.modelo.Usuario;
import pe.edu.upeu.CafeSnoopy.servicio.UsuarioServicioI;

import java.io.IOException;

@Controller
public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private CheckBox chkRecordar;

    // --- Inyección de dependencias de Spring ---
    @Autowired
    private UsuarioServicioI usuarioServicio;

    @Autowired
    private ApplicationContext context;

    @FXML
    public void initialize() {
        // Cargar usuario recordado si existe
        cargarUsuarioRecordado();
    }

    @FXML
    public void handleLogin() {
        if (validarCampos()) {
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText();

            // --- Lógica de autenticación ---
            if (autenticarUsuario(username, password)) {
                // Guardar preferencias si está marcado recordar
                if (chkRecordar.isSelected()) {
                    guardarUsuarioRecordado(username);
                } else {
                    limpiarUsuarioRecordado();
                }

                // Abrir ventana principal
                abrirVentanaPrincipal();
            } else {
                mostrarAlerta("Error de Autenticación",
                        "Usuario o contraseña incorrectos. Por favor, intente nuevamente.",
                        Alert.AlertType.ERROR);
                txtPassword.clear();
                txtPassword.requestFocus();
            }
        }
    }

    private boolean validarCampos() {
        if (txtUsername.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "Por favor ingrese su usuario", Alert.AlertType.WARNING);
            txtUsername.requestFocus();
            return false;
        }
        if (txtPassword.getText().isEmpty()) {
            mostrarAlerta("Validación", "Por favor ingrese su contraseña", Alert.AlertType.WARNING);
            txtPassword.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * MÉTODO CORREGIDO:
     * Autentica al usuario contra el UsuarioServicioI y mantiene al 'admin'
     */
    private boolean autenticarUsuario(String username, String password) {
        try {
            // 1. Mantener al usuario 'admin' como super-administrador
            if ("wagner".equals(username) && "123".equals(password)) {
                return true;
            }

            // 2. Buscar en el servicio (de tu repositorio)
            // Usamos equalsIgnoreCase para el username, pero equals para la contraseña
            for (Usuario user : usuarioServicio.findAll()) {
                if (user.getUsername().get().equalsIgnoreCase(username) &&
                        user.getPassword().get().equals(password)) {
                    return true;
                }
            }

            // 3. Si no se encuentra, retorna falso
            return false;

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al conectar con el sistema: " + e.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }

    // El método 'autenticacionEjemplo' que tenías antes ya no es necesario

    private void abrirVentanaPrincipal() {
        try {
            // Cerrar ventana de login
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.close();

            // Abrir ventana principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/maingui.fxml"));
            loader.setControllerFactory(context::getBean); // IMPORTANTE para Spring
            Parent root = loader.load();

            Stage mainStage = new Stage();
            mainStage.setTitle("Cafe Snoopy - Sistema de Gestión");
            mainStage.setScene(new Scene(root, 1200, 800));
            mainStage.setMaximized(true);
            mainStage.show();

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la interfaz principal: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void cargarUsuarioRecordado() {
        // Dejamos a 'admin' por defecto para pruebas rápidas
        txtUsername.setText("wagner");
    }

    private void guardarUsuarioRecordado(String username) {
        // Implementar guardado de preferencias
        System.out.println("Usuario recordado: " + username);
    }

    private void limpiarUsuarioRecordado() {
        // Implementar limpieza de preferencias
        System.out.println("Preferencias limpiadas");
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Estos métodos serán llamados por los botones del FXML
    @FXML
    private void handleRecuperarPassword() {
        mostrarAlerta("Recuperar Contraseña",
                "Por favor contacte al administrador del sistema para recuperar su contraseña.",
                Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleCrearCuenta() {
        mostrarAlerta("Crear Cuenta",
                "Para crear una nueva cuenta, contacte al administrador del sistema.",
                Alert.AlertType.INFORMATION);
    }
}