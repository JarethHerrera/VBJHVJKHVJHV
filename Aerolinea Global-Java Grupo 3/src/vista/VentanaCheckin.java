package vista;

import modelo.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class VentanaCheckin extends JFrame {

    private Almacen almacen = Almacen.getInstance();
    private Tiquete tiqueteActual;

    private JTextField txtBuscar;
    private JLabel lblInfo;
    private JPanel panelMaletas;
    private JTextField[] txtPesos;
    private JSpinner spnCantMaletas;
    private JLabel lblCobro;

    public VentanaCheckin() {
        setTitle("Módulo 4 - Check-in y Control de Equipaje");
        setSize(700, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Búsqueda
        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        busqueda.setBorder(BorderFactory.createTitledBorder("Buscar Reserva"));
        txtBuscar = new JTextField(12);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(0, 120, 215));
        btnBuscar.setForeground(Color.WHITE);
        lblInfo = new JLabel("Ingrese número de tiquete o ID de pasajero");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        busqueda.add(new JLabel("Tiquete / ID:"));
        busqueda.add(txtBuscar);
        busqueda.add(btnBuscar);
        busqueda.add(lblInfo);

        // Panel de equipaje
        JPanel equipaje = new JPanel(new BorderLayout(8, 8));
        equipaje.setBorder(BorderFactory.createTitledBorder("Registro de Equipaje"));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Cantidad de maletas (máx 5):"));
        spnCantMaletas = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        JButton btnGenerar = new JButton("Generar Campos");
        btnGenerar.setBackground(new Color(100, 100, 200));
        btnGenerar.setForeground(Color.WHITE);
        top.add(spnCantMaletas);
        top.add(btnGenerar);

        panelMaletas = new JPanel(new GridLayout(0, 2, 8, 6));
        panelMaletas.setBorder(new EmptyBorder(10, 10, 10, 10));

        lblCobro = new JLabel("Cargo por exceso: $0.00", SwingConstants.CENTER);
        lblCobro.setFont(new Font("Arial", Font.BOLD, 15));
        lblCobro.setForeground(new Color(180, 50, 0));

        JButton btnCheckin = new JButton("✔  Confirmar Check-in");
        btnCheckin.setBackground(new Color(0, 150, 50));
        btnCheckin.setForeground(Color.WHITE);
        btnCheckin.setFont(new Font("Arial", Font.BOLD, 13));

        equipaje.add(top, BorderLayout.NORTH);
        equipaje.add(new JScrollPane(panelMaletas), BorderLayout.CENTER);

        JPanel sur = new JPanel(new BorderLayout(5, 5));
        sur.add(lblCobro, BorderLayout.CENTER);
        sur.add(btnCheckin, BorderLayout.SOUTH);
        equipaje.add(sur, BorderLayout.SOUTH);

        main.add(busqueda, BorderLayout.NORTH);
        main.add(equipaje, BorderLayout.CENTER);
        setContentPane(main);

        btnBuscar.addActionListener(e -> buscarReserva());
        btnGenerar.addActionListener(e -> generarCamposMaletas());
        btnCheckin.addActionListener(e -> confirmarCheckin());
    }

    private void buscarReserva() {
        String val = txtBuscar.getText().trim().toUpperCase();
        tiqueteActual = almacen.buscarTiquete(val);
        if (tiqueteActual == null) tiqueteActual = almacen.buscarTiquetePorPasajero(val.toLowerCase());
        if (tiqueteActual == null) {
            lblInfo.setText("❌ Reserva no encontrada.");
            lblInfo.setForeground(Color.RED);
        } else {
            lblInfo.setText("✔ " + tiqueteActual.getPasajero().getNombreCompleto()
                    + " | Vuelo: " + tiqueteActual.getVuelo().getCodigo()
                    + " | Estado: " + tiqueteActual.getEstado());
            lblInfo.setForeground(new Color(0, 100, 0));
        }
    }

    private void generarCamposMaletas() {
        if (tiqueteActual == null) {
            JOptionPane.showMessageDialog(this, "Busque una reserva primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int cant = (int) spnCantMaletas.getValue();
        panelMaletas.removeAll();
        txtPesos = new JTextField[cant];
        for (int i = 0; i < cant; i++) {
            panelMaletas.add(new JLabel("Peso maleta " + (i + 1) + " (kg):"));
            txtPesos[i] = new JTextField("10.0");
            panelMaletas.add(txtPesos[i]);
        }
        panelMaletas.revalidate();
        panelMaletas.repaint();
    }

    private void confirmarCheckin() {
        if (tiqueteActual == null || txtPesos == null) {
            JOptionPane.showMessageDialog(this, "Complete los pasos anteriores.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double cargoTotal = 0;
        double PESO_MAX = 23.0;
        double TARIFA_KG_EXTRA = 10.0;
        StringBuilder detalle = new StringBuilder("Detalle de maletas:\n");
        try {
            for (int i = 0; i < txtPesos.length; i++) {
                double peso = Double.parseDouble(txtPesos[i].getText().trim());
                double exceso = Math.max(0, peso - PESO_MAX);
                double cargo = exceso * TARIFA_KG_EXTRA;
                cargoTotal += cargo;
                detalle.append(String.format("  Maleta %d: %.1f kg%s\n", i + 1, peso,
                        exceso > 0 ? " → Exceso: " + exceso + "kg ($" + cargo + ")" : " ✔"));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese pesos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        lblCobro.setText("Cargo por exceso: $" + String.format("%.2f", cargoTotal));
        if (cargoTotal > 0) {
            almacen.sumarPenalizacionEquipaje(cargoTotal);
            detalle.append("\nCargo total por exceso de equipaje: $").append(String.format("%.2f", cargoTotal));
        }
        tiqueteActual.setEstado("Check-in Realizado");
        detalle.append("\n\n✔ Check-in realizado exitosamente.");
        JOptionPane.showMessageDialog(this, detalle.toString(), "Check-in Confirmado", JOptionPane.INFORMATION_MESSAGE);
    }
}
