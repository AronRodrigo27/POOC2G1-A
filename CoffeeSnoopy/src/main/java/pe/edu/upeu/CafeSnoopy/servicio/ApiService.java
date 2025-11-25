package pe.edu.upeu.CafeSnoopy.servicio;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ApiService {

    // 👇 AQUÍ PEGAS EL TOKEN NUEVO QUE ACABAS DE CONSEGUIR EN APIPERU.DEV
    private static final String TOKEN = "7632c26fa780391a99b4c600511a9cf3dfc9da758e49b6dafee12e1748cbf543";

    // Esta es la URL moderna
    private static final String URL_BASE = "https://apiperu.dev/api/dni/";

    public String buscarPersona(String dni) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE + dni))
                    .header("Authorization", "Bearer " + TOKEN) // El token se envía aquí
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject json = new JSONObject(response.body());
                if (json.getBoolean("success")) {
                    JSONObject data = json.getJSONObject("data");
                    // Construimos el nombre completo
                    return data.getString("nombres") + " " +
                            data.getString("apellido_paterno") + " " +
                            data.getString("apellido_materno");
                }
            }
        } catch (Exception e) {
            System.err.println("Error en API Actualizada: " + e.getMessage());
        }
        return null;
    }
}