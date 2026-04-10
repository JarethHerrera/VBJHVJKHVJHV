package vista;

import modelo.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class VentanaReportes extends JFrame {

    private Almacen almacen = Almacen.getInstance();

    public VentanaReportes() {
        setTitle("Reportes del Sistema");
        setSize(720, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 13));
        tabs.addTab("📊 Ocupación por Vuelo", crearTabOcupacion());
        tabs.addTab("🍽 Manifiesto Servicios", crearTabManifiesto());
        tabs.addTab("💰 Resumen Financiero", crearTabFinanciero());
        add(tabs);
    }

    private JPanel crearTabOcupacion() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JComboBox<String> cmbVuelos = new JComboBox<>();
        for (int i = 0; i < almacen.getCantVuelos(); i++) cmbVuelos.addItem(almacen.getVuelos()[i].toString());
        JButton btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setBackground(new Color(0, 120, 215)); btnGenerar.setForeground(Color.WHITE);
        JTextArea area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setEditable(false);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Vuelo:")); top.add(cmbVuelos); top.add(btnGenerar);
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        btnGenerar.addActionListener(e -> {
            if (almacen.getCantVuelos() == 0) { area.setText("No hay vuelos registrados."); return; }
            int idx = cmbVuelos.getSelectedIndex();
            Vuelo v = almacen.getVuelos()[idx];
            Avion a = v.getAvion();
            Asiento[][] seats = a.getAsientos();
            int totFilas = a.getFilasPrimera() + a.getFilasEjecutiva() + a.getFilasEconomica();
            int ocuPrimera = 0, totPrimera = a.getFilasPrimera() * a.getColumnas();
            int ocuEjec = 0, totEjec = a.getFilasEjecutiva() * a.getColumnas();
            int ocuEcon = 0, totEcon = a.getFilasEconomica() * a.getColumnas();
            for (int i = 0; i < totFilas; i++) {
                for (int j = 0; j < a.getColumnas(); j++) {
                    if (seats[i][j].isOcupado()) {
                        switch (seats[i][j].getZona()) {
                            case "Primera": ocuPrimera++; break;
                            case "Ejecutiva": ocuEjec++; break;
                            default: ocuEcon++;
                        }
                    }
                }
            }
            int total = a.getTotalAsientos();
            int ocuTotal = ocuPrimera + ocuEjec + ocuEcon;
            StringBuilder sb = new StringBuilder();
            sb.append("══════════════════════════════════════\n");
            sb.append("  REPORTE DE OCUPACIÓN - VUELO ").append(v.getCodigo()).append("\n");
            sb.append("══════════════════════════════════════\n");
            sb.append(String.format("Ruta    : %s → %s\n", v.getOrigen(), v.getDestino()));
            sb.append(String.format("Fecha   : %s\n", v.getFecha()));
            sb.append(String.format("Avión   : %s\n\n", a.toString()));
            sb.append(String.format("%-15s %5s / %-5s  %6.1f%%\n", "1ra Clase:", ocuPrimera, totPrimera, totPrimera>0?(double)ocuPrimera/totPrimera*100:0));
            sb.append(String.format("%-15s %5s / %-5s  %6.1f%%\n", "Ejecutiva:", ocuEjec, totEjec, totEjec>0?(double)ocuEjec/totEjec*100:0));
            sb.append(String.format("%-15s %5s / %-5s  %6.1f%%\n", "Económica:", ocuEcon, totEcon, totEcon>0?(double)ocuEcon/totEcon*100:0));
            sb.append("──────────────────────────────────────\n");
            sb.append(String.format("%-15s %5s / %-5s  %6.1f%%\n", "TOTAL:", ocuTotal, total, (double)ocuTotal/total*100));
            if ((double)ocuTotal/total > 0.80) sb.append("\n⚠ Ocupación >80%: se aplica tarifa de demanda (+20%)\n");
            area.setText(sb.toString());
        });
        return panel;
    }

    private JPanel crearTabManifiesto() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JComboBox<String> cmbVuelos = new JComboBox<>();
        for (int i = 0; i < almacen.getCantVuelos(); i++) cmbVuelos.addItem(almacen.getVuelos()[i].toString());
        JButton btnGenerar = new JButton("Generar Manifiesto");
        btnGenerar.setBackground(new Color(150, 80, 200)); btnGenerar.setForeground(Color.WHITE);
        JTextArea area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setEditable(false);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Vuelo:")); top.add(cmbVuelos); top.add(btnGenerar);
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        btnGenerar.addActionListener(e -> {
            if (almacen.getCantVuelos() == 0) { area.setText("No hay vuelos."); return; }
            int idx = cmbVuelos.getSelectedIndex();
            Vuelo v = almacen.getVuelos()[idx];
            StringBuilder sb = new StringBuilder();
            sb.append("══════════════════════════════════════════════\n");
            sb.append("  MANIFIESTO DE SERVICIOS ESPECIALES\n");
            sb.append("  Vuelo: ").append(v.getCodigo()).append("  ").append(v.getOrigen()).append(" → ").append(v.getDestino()).append("\n");
            sb.append("══════════════════════════════════════════════\n");
            sb.append(String.format("%-10s %-22s %-12s %-14s\n", "Asiento", "Pasajero", "Menú", "Clase"));
            sb.append("──────────────────────────────────────────────\n");
            boolean hayEspeciales = false;
            for (int i = 0; i < almacen.getCantTiquetes(); i++) {
                Tiquete t = almacen.getTiquetes()[i];
                if (t.getVuelo().getCodigo().equals(v.getCodigo()) && t.getEstado().equals("Activo")) {
                    if (!t.getMenuEspecial().equals("Estándar")) {
                        sb.append(String.format("%-10s %-22s %-12s %-14s\n",
                                t.getAsiento().getCodigo(),
                                t.getPasajero().getNombreCompleto(),
                                t.getMenuEspecial(),
                                t.getAsiento().getZona()));
                        hayEspeciales = true;
                    }
                }
            }
            if (!hayEspeciales) sb.append("No hay solicitudes de menú especial para este vuelo.\n");
            area.setText(sb.toString());
        });
        return panel;
    }

    private JPanel crearTabFinanciero() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton btnGenerar = new JButton("Generar Resumen Financiero");
        btnGenerar.setBackground(new Color(200, 130, 0)); btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setFont(new Font("Arial", Font.BOLD, 13));
        JTextArea area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setEditable(false);

        panel.add(btnGenerar, BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        btnGenerar.addActionListener(e -> {
            double t = almacen.getIngresosTiquetes();
            double a = almacen.getIngresosAbordo();
            double pe = almacen.getIngresosPenalizacionEquipaje();
            double pc = almacen.getIngresosPenalizacionCancelacion();
            double tot = almacen.getTotalIngresos();
            StringBuilder sb = new StringBuilder();
            sb.append("══════════════════════════════════════\n");
            sb.append("     RESUMEN FINANCIERO - AEROLÍNEA\n");
            sb.append("══════════════════════════════════════\n\n");
            sb.append(String.format("%-30s $%10.2f\n", "Venta de Tiquetes:", t));
            sb.append(String.format("%-30s $%10.2f\n", "Ventas a Bordo (Duty Free):", a));
            sb.append(String.format("%-30s $%10.2f\n", "Penaliz. por Equipaje:", pe));
            sb.append(String.format("%-30s $%10.2f\n", "Penaliz. por Cancelación:", pc));
            sb.append("──────────────────────────────────────\n");
            sb.append(String.format("%-30s $%10.2f\n", "TOTAL RECAUDADO:", tot));
            sb.append("\n══════════════════════════════════════\n");
            area.setText(sb.toString());
        });
        return panel;
    }
}
