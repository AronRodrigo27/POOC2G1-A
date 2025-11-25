package pe.edu.upeu.CafeSnoopy.control;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.CafeSnoopy.modelo.Venta;
import pe.edu.upeu.CafeSnoopy.repositorio.DetalleVentaRepository;
import pe.edu.upeu.CafeSnoopy.repositorio.VentaRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ReportesController {

    @FXML private ComboBox<String> comboTipoReporte;
    @FXML private DatePicker dateInicio, dateFin;
    @FXML private BarChart<String, Number> barChartVentas;
    @FXML private PieChart pieChartCategorias;
    @FXML private LineChart<String, Number> lineChartTendencia;

    @Autowired private VentaRepository ventaRepo;
    @Autowired private DetalleVentaRepository detalleRepo;

    @FXML
    public void initialize() {
        comboTipoReporte.getItems().addAll("Resumen General", "Ventas por Categoría");
        comboTipoReporte.getSelectionModel().selectFirst();
        generarReporte(); // Cargar gráficos al iniciar
    }

    @FXML
    public void generarReporte() {
        cargarGraficoVentasSemana();
        cargarGraficoTopProductos();
    }

    private void cargarGraficoVentasSemana() {
        if(barChartVentas == null) return;
        barChartVentas.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ventas (S/)");

        List<Object[]> datos = ventaRepo.reporteVentasUltimaSemana();
        for(Object[] fila : datos) {
            // fila[0] = fecha (String), fila[1] = total (BigDecimal)
            if (fila[0] != null && fila[1] != null) {
                series.getData().add(new XYChart.Data<>(fila[0].toString(), (Number) fila[1]));
            }
        }
        barChartVentas.getData().add(series);
    }

    private void cargarGraficoTopProductos() {
        if(pieChartCategorias == null) return;
        pieChartCategorias.getData().clear();

        List<Object[]> datos = detalleRepo.obtenerProductosMasVendidos();
        for(Object[] fila : datos) {
            // fila[0] = nombre, fila[1] = cantidad
            if (fila[0] != null && fila[1] != null) {
                String nombre = fila[0].toString();
                Number cantidad = (Number) fila[1];
                pieChartCategorias.getData().add(new PieChart.Data(nombre + " (" + cantidad + ")", cantidad.doubleValue()));
            }
        }
    }

    // --- FUNCIONALIDAD DE EXPORTACIÓN A EXCEL (CSV) ---
    @FXML
    public void exportarExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV (Excel)", "*.csv"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                // Encabezados
                writer.println("ID Venta,Fecha,Cliente,Total");

                // Datos
                List<Venta> ventas = ventaRepo.findAll();
                for (Venta v : ventas) {
                    writer.printf("%d,%s,%s,%.2f%n",
                            v.getIdVenta(),
                            v.getFechaVenta().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            (v.getNombreCliente() != null ? v.getNombreCliente() : "General"),
                            v.getTotalVenta());
                }
                mostrarAlerta("Éxito", "Reporte exportado correctamente a: " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo exportar: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    // --- FUNCIONALIDAD DE EXPORTACIÓN A PDF (EL PASO 4) ---
    @FXML
    public void exportarPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            Document documento = new Document(PageSize.A4);
            try {
                PdfWriter.getInstance(documento, new FileOutputStream(file));
                documento.open();

                // 1. Título del Documento
                Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
                Paragraph titulo = new Paragraph("Reporte General de Ventas - Cafe Snoopy", fuenteTitulo);
                titulo.setAlignment(Element.ALIGN_CENTER);
                titulo.setSpacingAfter(20);
                documento.add(titulo);

                // 2. Información General (Opcional)
                Font fuenteInfo = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
                documento.add(new Paragraph("Generado el: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), fuenteInfo));
                documento.add(new Paragraph(" ")); // Espacio en blanco

                // 3. Tabla de Datos
                PdfPTable tabla = new PdfPTable(4); // 4 columnas
                tabla.setWidthPercentage(100);
                tabla.setWidths(new float[]{1, 2, 3, 2}); // Anchos relativos de columnas

                // Estilo de Encabezados
                Font fuenteHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);

                // Método auxiliar para crear celdas de cabecera
                agregarCeldaCabecera(tabla, "ID", fuenteHeader);
                agregarCeldaCabecera(tabla, "Fecha", fuenteHeader);
                agregarCeldaCabecera(tabla, "Cliente", fuenteHeader);
                agregarCeldaCabecera(tabla, "Total", fuenteHeader);

                // Llenar datos desde la BD
                List<Venta> ventas = ventaRepo.findAll();
                BigDecimal totalGeneral = BigDecimal.ZERO;

                for (Venta v : ventas) {
                    tabla.addCell(String.valueOf(v.getIdVenta()));
                    tabla.addCell(v.getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                    tabla.addCell(v.getNombreCliente() != null ? v.getNombreCliente() : "Varios");
                    tabla.addCell("S/ " + v.getTotalVenta());

                    totalGeneral = totalGeneral.add(v.getTotalVenta());
                }

                documento.add(tabla);

                // 4. Total General al final
                documento.add(new Paragraph(" "));
                Font fuenteTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.RED);
                Paragraph totalParrafo = new Paragraph("Total Ingresos: S/ " + totalGeneral, fuenteTotal);
                totalParrafo.setAlignment(Element.ALIGN_RIGHT);
                documento.add(totalParrafo);

                documento.close();

                mostrarAlerta("Éxito", "PDF generado correctamente en:\n" + file.getAbsolutePath(), Alert.AlertType.INFORMATION);

            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error", "No se pudo generar el PDF: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    // Método auxiliar para estilizar las cabeceras de la tabla PDF
    private void agregarCeldaCabecera(PdfPTable tabla, String texto, Font fuente) {
        var celda = new com.itextpdf.text.pdf.PdfPCell(new Phrase(texto, fuente));
        celda.setBackgroundColor(BaseColor.DARK_GRAY);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(8);
        tabla.addCell(celda);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}