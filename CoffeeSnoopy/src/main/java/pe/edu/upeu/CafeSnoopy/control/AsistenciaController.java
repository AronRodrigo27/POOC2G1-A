package pe.edu.upeu.CafeSnoopy.control;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Controller;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AsistenciaController {

    @FXML private TextField txtNum1, txtNum2;
    @FXML private Label result;
    @FXML private ComboBox<String> comboOperacion;
    @FXML private DatePicker datePicker;
    @FXML private TableView<Operacion> tableHistorial;
    @FXML private Label labelTotal, labelCorrectos, labelErrores;

    private List<Operacion> historial = new ArrayList<>();
    private int totalCalculos = 0;
    private int correctos = 0;
    private int errores = 0;

    @FXML
    public void initialize() {
        configurarComboBox();
        configurarTabla();
        actualizarEstadisticas();
    }

    private void configurarComboBox() {
        comboOperacion.getItems().addAll("Suma (+)", "Resta (-)", "Multiplicación (×)", "División (÷)");
        comboOperacion.getSelectionModel().selectFirst();
    }

    private void configurarTabla() {
        TableColumn<Operacion, String> colFecha = new TableColumn<>("Fecha");
        TableColumn<Operacion, String> colOperacion = new TableColumn<>("Operación");
        TableColumn<Operacion, String> colNum1 = new TableColumn<>("Número 1");
        TableColumn<Operacion, String> colNum2 = new TableColumn<>("Número 2");
        TableColumn<Operacion, String> colResultado = new TableColumn<>("Resultado");
        TableColumn<Operacion, String> colEstado = new TableColumn<>("Estado");

        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colOperacion.setCellValueFactory(new PropertyValueFactory<>("operacion"));
        colNum1.setCellValueFactory(new PropertyValueFactory<>("numero1"));
        colNum2.setCellValueFactory(new PropertyValueFactory<>("numero2"));
        colResultado.setCellValueFactory(new PropertyValueFactory<>("resultado"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tableHistorial.getColumns().addAll(colFecha, colOperacion, colNum1, colNum2, colResultado, colEstado);
    }

    @FXML
    private void calcular() {
        try {
            double num1 = Double.parseDouble(txtNum1.getText());
            double num2 = Double.parseDouble(txtNum2.getText());
            String operacion = comboOperacion.getValue();
            double resultado = 0;
            boolean error = false;

            switch (operacion) {
                case "Suma (+)":
                    resultado = num1 + num2;
                    break;
                case "Resta (-)":
                    resultado = num1 - num2;
                    break;
                case "Multiplicación (×)":
                    resultado = num1 * num2;
                    break;
                case "División (÷)":
                    if (num2 == 0) {
                        result.setText("Error: División por cero");
                        error = true;
                    } else {
                        resultado = num1 / num2;
                    }
                    break;
            }

            if (!error) {
                result.setText(String.format("%.2f", resultado));
                guardarOperacion(num1, num2, operacion, resultado, "✓ Correcto");
                correctos++;
            } else {
                guardarOperacion(num1, num2, operacion, 0, "✗ Error");
                errores++;
            }

            totalCalculos++;
            actualizarEstadisticas();

        } catch (NumberFormatException e) {
            result.setText("Error: Entrada inválida");
            guardarOperacion(0, 0, comboOperacion.getValue(), 0, "✗ Error");
            errores++;
            totalCalculos++;
            actualizarEstadisticas();
        }
    }

    @FXML
    private void limpiar() {
        txtNum1.clear();
        txtNum2.clear();
        result.setText("0.00");
        comboOperacion.getSelectionModel().selectFirst();
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    private void guardar() {
        // Aquí puedes implementar la lógica para guardar en base de datos o archivo
        mostrarAlerta("Éxito", "Operación guardada correctamente");
    }

    @FXML
    private void cancelar() {
        limpiar();
    }

    private void guardarOperacion(double num1, double num2, String operacion, double resultado, String estado) {
        LocalDate fecha = datePicker.getValue() != null ? datePicker.getValue() : LocalDate.now();
        String fechaStr = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Operacion op = new Operacion(
                fechaStr,
                operacion,
                String.valueOf(num1),
                String.valueOf(num2),
                String.format("%.2f", resultado),
                estado
        );

        historial.add(0, op); // Agregar al inicio de la lista
        tableHistorial.getItems().setAll(historial);
    }

    private void actualizarEstadisticas() {
        labelTotal.setText(String.valueOf(totalCalculos));
        labelCorrectos.setText(String.valueOf(correctos));
        labelErrores.setText(String.valueOf(errores));
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Clase interna para representar una operación
    public static class Operacion {
        private final String fecha;
        private final String operacion;
        private final String numero1;
        private final String numero2;
        private final String resultado;
        private final String estado;

        public Operacion(String fecha, String operacion, String numero1, String numero2, String resultado, String estado) {
            this.fecha = fecha;
            this.operacion = operacion;
            this.numero1 = numero1;
            this.numero2 = numero2;
            this.resultado = resultado;
            this.estado = estado;
        }

        public String getFecha() { return fecha; }
        public String getOperacion() { return operacion; }
        public String getNumero1() { return numero1; }
        public String getNumero2() { return numero2; }
        public String getResultado() { return resultado; }
        public String getEstado() { return estado; }
    }
}