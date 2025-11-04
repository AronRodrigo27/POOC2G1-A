package pe.edu.upeu.CafeSnoopy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URL;

@SpringBootApplication
public class AsistenciaApplication extends Application {

    private ConfigurableApplicationContext context;
    private Parent parent;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(AsistenciaApplication.class);
        builder.application().setWebApplicationType(WebApplicationType.NONE);
        context = builder.run(getParameters().getRaw().toArray(new String[0]));

        // --- ⬇️ CAMBIO AQUÍ ⬇️ ---
        // 1. Apuntamos al login, no al maingui.
        URL fxmlUrl = getClass().getResource("/fxml/main_login.fxml");
        if (fxmlUrl == null) {
            throw new IllegalStateException("No se pudo encontrar 'main_login.fxml' en /fxml/");
        }
        // --- ⬆️ FIN DEL CAMBIO ⬆️ ---

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        loader.setControllerFactory(context::getBean);
        parent = loader.load();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // --- ⬇️ CAMBIO AQUÍ ⬇️ ---
        // 2. Ajustamos el tamaño y título de la ventana de login
        // (Tu FXML de login está diseñado para 900x600)
        stage.setScene(new Scene(parent, 900, 600));
        stage.setTitle("Cafe Snoopy - Iniciar Sesión");
        // --- ⬆️ FIN DEL CAMBIO ⬆️ ---
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        if (context != null) {
            context.close();
        }
    }
}