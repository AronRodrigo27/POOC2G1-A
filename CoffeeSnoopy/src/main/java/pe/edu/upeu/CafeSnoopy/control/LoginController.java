package pe.edu.upeu.CafeSnoopy.control;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.CafeSnoopy.modelo.Usuario;
import pe.edu.upeu.CafeSnoopy.servicio.UsuarioServicioI;

import java.io.IOException;
import java.util.Optional;

@Controller
public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private CheckBox chkRecordar;

    @Autowired
    private UsuarioServicioI usuarioServicio;

    @Autowired
    private ApplicationContext context;

    @FXML
    public void initialize() {
        cargarUsuarioRecordado();
    }

    @FXML
    public void handleLogin() {
        if (validarCampos()) {
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText();

            System.out.println("=== INTENTO DE LOGIN ===");
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            System.out.println("=========================");

            if (autenticarUsuario(username, password)) {
                if (chkRecordar.isSelected()) {
                    guardarUsuarioRecordado(username);
                } else {
                    limpiarUsuarioRecordado();
                }

                abrirVentanaPrincipal();
            } else {
                // El mensaje de error ahora se muestra dentro de autenticarUsuario
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
     * Autentica al usuario usando el servicio JPA, buscando en la base de datos.
     */
    /**
     * Autentica al usuario usando el servicio JPA y guarda la sesión.
     */
    private boolean autenticarUsuario(String username, String password) {
        try {
            // 1. Buscar el usuario por username en la base de datos
            java.util.Optional<Usuario> usuarioOpt = usuarioServicio.findByUsername(username);

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();

                // 2. Verificar la contraseña
                if (usuario.getPassword().equals(password)) {
                    // 3. Verificar el estado
                    if (usuario.getEstado()) {
                        // ✅ GUARDAR SESIÓN AQUÍ
                        pe.edu.upeu.CafeSnoopy.utils.SesionGlobal.usuarioLogueado = usuario;

                        System.out.println("✅ Autenticación exitosa para: " + usuario.getUsername());
                        return true;
                    } else {
                        mostrarAlerta("Acceso Denegado", "Su cuenta está inactiva. Contacte al administrador.", Alert.AlertType.WARNING);
                        return false;
                    }
                } else {
                    mostrarAlerta("Error de Autenticación", "Contraseña incorrecta.", Alert.AlertType.ERROR);
                    return false;
                }
            } else {
                mostrarAlerta("Error de Autenticación", "Usuario no encontrado.", Alert.AlertType.ERROR);
                return false;
            }

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al conectar con la base de datos: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
            return false;
        }
    }

    private void abrirVentanaPrincipal() {
        try {
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/maingui.fxml"));
            loader.setControllerFactory(context::getBean);
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

    // [El resto de métodos se mantiene]
    private void cargarUsuarioRecordado() {
        txtUsername.setText("Snoopy"); // Ejemplo por defecto
    }

    private void guardarUsuarioRecordado(String username) {
        System.out.println("Usuario recordado: " + username);
    }

    private void limpiarUsuarioRecordado() {
        System.out.println("Preferencias limpiadas");
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void handleRecuperarPassword() {
        mostrarAlerta("Recuperar Contraseña",
                "Por favor contacte al administrador del sistema para recuperar su contraseña.",
                Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleCrearCuenta() {
        try {
            // Cargar la ventana de registro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registro_usuario.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();

            // Crear nueva ventana
            Stage registroStage = new Stage();
            registroStage.setTitle("Cafe Snoopy - Crear Nueva Cuenta");
            registroStage.setScene(new Scene(root, 450, 550));
            registroStage.setResizable(false);

            // Hacerla modal para que bloquee la ventana principal
            registroStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

            registroStage.showAndWait(); // showAndWait() para esperar a que se cierre

        } catch (IOException e) {
            mostrarAlerta("Error",
                    "No se pudo cargar el formulario de registro: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}