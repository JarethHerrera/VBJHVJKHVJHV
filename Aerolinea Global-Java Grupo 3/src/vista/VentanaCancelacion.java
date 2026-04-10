package vista;

import modelo.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class VentanaCancelacion extends JFrame {

    private Almacen almacen = Almacen.getInstance();
    private Tiquete tiqueteActual;
    private JTextField txtBuscar;
    private JLabel lblInfo, lblReembolso;

    public VentanaCancelacion() {
        setTitle("Módulo 5 - Cancelaciones y Reembolsos");
        setSize(600, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        busqueda.setBorder(BorderFactory.createTitledBorder("Buscar Tiquete / Pasajero"));
        txtBuscar = new JTextField(14);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(0, 120, 215));
        btnBuscar.setForeground(Color.WHITE);
        busqueda.add(new JLabel("Nº Tiquete o ID:"));
        busqueda.add(txtBuscar);
        busqueda.add(btnBuscar);

        lblInfo = new JLabel("Ingrese el número de tiquete o cédula del pasajero", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblInfo.setBorder(BorderFactory.createTitledBorder("Información de Reserva"));
        lblInfo.setPreferredSize(new Dimension(0, 80));

        lblReembolso = new JLabel("", SwingConstants.CENTER);
        lblReembolso.setFont(new Font("Arial", Font.BOLD, 15));
        lblReembolso.setForeground(new Color(0, 100, 0));

        JButton btnCancelar = new JButton("❌  Cancelar Tiquete");
        btnCancelar.setBackground(new Color(200, 50, 50));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel centro = new JPanel(new GridLayout(3, 1, 8, 8));
        centro.add(lblInfo);
        centro.add(lblReembolso);
        centro.add(btnCancelar);

        main.add(busqueda, BorderLayout.NORTH);
        main.add(centro, BorderLayout.CENTER);
        setContentPane(main);

        btnBuscar.addActionListener(e -> buscarTiquete());
        btnCancelar.addActionListener(e -> cancelarTiquete());
    }

    private void buscarTiquete() {
        String val = txtBuscar.getText().trim().toUpperCase();
        tiqueteActual = almacen.buscarTiquete(val);
        if (tiqueteActual == null) tiqueteActual = almacen.buscarTiquetePorPasajero(txtBuscar.getText().trim());
        if (tiqueteActual == null) {
            lblInfo.setText("❌ No encontrado.");
            lblReembolso.setText("");
        } else {
            String nivel = tiqueteActual.getPasajero().getNivelSocio();
            double pagado = tiqueteActual.getPrecioPagado();
            double reembolso = nivel.equals("Platino") ? pagado : pagado * 0.70;
            double penalidad = pagado - reembolso;
            lblInfo.setText("<html><center>"
                    + tiqueteActual.getNumero() + " | " + tiqueteActual.getPasajero().getNombreCompleto()
                    + "<br>Vuelo: " + tiqueteActual.getVuelo().getCodigo()
                    + " | Asiento: " + tiqueteActual.getAsiento().getCodigo()
                    + " | Estado: " + tiqueteActual.getEstado() + "</center></html>");
            lblReembolso.setText(String.format(
                    "<html><center>Reembolso: $%.2f | Penalidad: $%.2f (Nivel: %s)</center></html>",
                    reembolso, penalidad, nivel));
        }
    }

    private void cancelarTiquete() {
        if (tiqueteActual == null) {
            JOptionPane.showMessageDialog(this, "Busque un tiquete primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!tiqueteActual.getEstado().equals("Activo")) {
            JOptionPane.showMessageDialog(this, "Este tiquete no está activo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nivel = tiqueteActual.getPasajero().getNivelSocio();
        double pagado = tiqueteActual.getPrecioPagado();
        double reembolso = nivel.equals("Platino") ? pagado : pagado * 0.70;
        double penalidad = pagado - reembolso;
        int confirm = JOptionPane.showConfirmDialog(this,
                String.format("¿Confirmar cancelación?\n\nReembolso: $%.2f\nPenalidad: $%.2f", reembolso, penalidad),
                "Confirmar Cancelación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            tiqueteActual.getAsiento().liberar();
            tiqueteActual.setEstado("Cancelado");
            almacen.sumarPenalizacionCancelacion(penalidad);
            JOptionPane.showMessageDialog(this,
                    String.format("Tiquete cancelado.\nReembolso: $%.2f\nEl asiento ha sido liberado.", reembolso),
                    "Cancelación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            tiqueteActual = null;
            lblInfo.setText("Ingrese el número de tiquete o cédula del pasajero");
            lblReembolso.setText("");
        }
    }
}
