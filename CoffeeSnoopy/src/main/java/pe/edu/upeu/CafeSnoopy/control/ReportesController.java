package pe.edu.upeu.CafeSnoopy.control;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import org.springframework.stereotype.Controller;

@Controller
public class ReportesController {

    @FXML
    private PieChart pieChartCategorias;

    @FXML
    private BarChart<String, Number> barChartVentas;

    @FXML
    private LineChart<String, Number> lineChartTendencia;

    @FXML
    private ComboBox<String> comboTipoReporte;

    @FXML
    public void initialize() {
        // Inicializar ComboBox
        comboTipoReporte.getItems().addAll(
                "Ventas Diarias",
                "Ventas Mensuales",
                "Productos Más Vendidos",
                "Clientes Frecuentes",
                "Rendimiento Vendedores"
        );

        // Inicializar gráficos
        inicializarGraficos();
    }

    private void inicializarGraficos() {
        // PieChart - Distribución por categoría
        PieChart.Data slice1 = new PieChart.Data("Bebidas Calientes", 45);
        PieChart.Data slice2 = new PieChart.Data("Bebidas Frías", 25);
        PieChart.Data slice3 = new PieChart.Data("Sandwiches", 15);
        PieChart.Data slice4 = new PieChart.Data("Postres", 10);
        PieChart.Data slice5 = new PieChart.Data("Otros", 5);

        pieChartCategorias.getData().addAll(slice1, slice2, slice3, slice4, slice5);

        // BarChart - Ventas de la semana
        XYChart.Series<String, Number> seriesVentas = new XYChart.Series<>();
        seriesVentas.setName("Ventas Diarias");
        seriesVentas.getData().add(new XYChart.Data<>("Lun", 1200));
        seriesVentas.getData().add(new XYChart.Data<>("Mar", 1800));
        seriesVentas.getData().add(new XYChart.Data<>("Mié", 1500));
        seriesVentas.getData().add(new XYChart.Data<>("Jue", 2200));
        seriesVentas.getData().add(new XYChart.Data<>("Vie", 2800));
        seriesVentas.getData().add(new XYChart.Data<>("Sáb", 3200));
        seriesVentas.getData().add(new XYChart.Data<>("Dom", 2500));
        barChartVentas.getData().add(seriesVentas);

        // LineChart - Tendencia mensual
        XYChart.Series<String, Number> seriesTendencia = new XYChart.Series<>();
        seriesTendencia.setName("Ventas Mensuales");
        seriesTendencia.getData().add(new XYChart.Data<>("Ene", 15250));
        seriesTendencia.getData().add(new XYChart.Data<>("Feb", 16800));
        seriesTendencia.getData().add(new XYChart.Data<>("Mar", 14200));
        seriesTendencia.getData().add(new XYChart.Data<>("Abr", 18900));
        seriesTendencia.getData().add(new XYChart.Data<>("May", 21000));
        seriesTendencia.getData().add(new XYChart.Data<>("Jun", 19500));
        lineChartTendencia.getData().add(seriesTendencia);
    }
}