package pe.edu.upeu.CafeSnoopy.servicio;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import pe.edu.upeu.CafeSnoopy.modelo.DetalleVenta;
import pe.edu.upeu.CafeSnoopy.modelo.Venta;

import javax.print.PrintService;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;

@Service
public class TicketService implements Printable {

    private Venta ventaActual;

    public void imprimirVenta(Venta venta) {
        this.ventaActual = venta;

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);

        // ⚠️ IMPORTANTE: AQUÍ VA EL NOMBRE EXACTO DE TU IMPRESORA EN WINDOWS
        // Ve a "Panel de Control" > "Dispositivos e Impresoras" y copia el nombre tal cual.
        // Si no lo encuentras, el código intentará usar la impresora predeterminada.
        String nombreImpresoraDeseada = "POS-80"; // <--- CAMBIA ESTO SI SE LLAMA DIFERENTE (Ej: "Bienex E8021")

        PrintService[] services = PrinterJob.lookupPrintServices();
        PrintService printService = null;

        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(nombreImpresoraDeseada)) {
                printService = service;
                break;
            }
        }

        if (printService != null) {
            try {
                job.setPrintService(printService);
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠️ No se encontró la impresora '" + nombreImpresoraDeseada + "'. Se intentará con la predeterminada.");
            if (job.printDialog()) { // Esto abre el cuadro de diálogo si no encuentra la específica
                try { job.print(); } catch (PrinterException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) return NO_SUCH_PAGE;

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        int y = 20;
        int x = 5;

        Font fuenteTitulo = new Font("Monospaced", Font.BOLD, 10);
        Font fuenteNormal = new Font("Monospaced", Font.PLAIN, 8);

        // --- CABECERA ---
        g2d.setFont(fuenteTitulo);
        g2d.drawString("CAFE SNOOPY", x + 20, y); y += 15;

        g2d.setFont(fuenteNormal);
        g2d.drawString("Fecha: " + ventaActual.getFechaVenta(), x, y); y += 10;
        g2d.drawString("Cliente: " + (ventaActual.getNombreCliente() == null ? "General" : ventaActual.getNombreCliente()), x, y); y += 10;
        g2d.drawString("--------------------------------", x, y); y += 10;

        // --- PRODUCTOS ---
        for (DetalleVenta dt : ventaActual.getDetalles()) {
            String prod = dt.getProducto().getNombre();
            if (prod.length() > 15) prod = prod.substring(0, 15);
            g2d.drawString(dt.getCantidad() + " x " + prod + "  S/" + dt.getSubtotal(), x, y);
            y += 10;
        }
        g2d.drawString("--------------------------------", x, y); y += 10;

        // --- TOTAL ---
        g2d.setFont(fuenteTitulo);
        g2d.drawString("TOTAL: S/ " + ventaActual.getTotalVenta(), x, y); y += 25;

        // --- QR ---
        try {
            String data = "VENTA:" + ventaActual.getIdVenta() + "|TOTAL:" + ventaActual.getTotalVenta();
            BufferedImage qr = generarQR(data, 80, 80);
            g2d.drawImage(qr, x + 30, y, null);
        } catch (Exception e) {
            g2d.drawString("[QR ERROR]", x, y);
        }

        return PAGE_EXISTS;
    }

    private BufferedImage generarQR(String data, int w, int h) throws Exception {
        return MatrixToImageWriter.toBufferedImage(new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, w, h));
    }
}