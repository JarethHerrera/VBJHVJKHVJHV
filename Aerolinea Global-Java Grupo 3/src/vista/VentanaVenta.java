package vista;

import modelo.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaVenta extends JFrame {

    private Almacen almacen = Almacen.getInstance();
    private Vuelo vueloSeleccionado;
    private Asiento asientoSeleccionado;

    private JComboBox<String> cmbVuelos;
    private JPanel panelMapa;
    private JLabel lblInfoVuelo, lblPrecio, lblAsientoSel;
    private JTextField txtIdPasajero, txtNombre, txtApellido, txtEmail;
    private JComboBox<String> cmbNivelSocio;
    private JButton btnComprar;
    private JButton[][] botonesAsientos;

    public VentanaVenta() {
        setTitle("Módulo 2 - Venta de Tiquetes");
        setSize(950, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel superior - selección de vuelo
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Selección de Vuelo"));
        cmbVuelos = new JComboBox<>();
        cargarVuelos();
        JButton btnSeleccionar = new JButton("Cargar Mapa");
        btnSeleccionar.setBackground(new Color(0, 120, 215));
        btnSeleccionar.setForeground(Color.WHITE);
        lblInfoVuelo = new JLabel("Seleccione un vuelo para ver disponibilidad");
        lblInfoVuelo.setFont(new Font("Arial", Font.ITALIC, 12));
        topPanel.add(new JLabel("Vuelo:"));
        topPanel.add(cmbVuelos);
        topPanel.add(btnSeleccionar);
        topPanel.add(lblInfoVuelo);

        // Panel central - mapa de asientos
        panelMapa = new JPanel();
        panelMapa.setBorder(BorderFactory.createTitledBorder("Mapa de Asientos"));
        panelMapa.setBackground(new Color(248, 248, 248));
        JScrollPane scrollMapa = new JScrollPane(panelMapa);
        scrollMapa.setPreferredSize(new Dimension(580, 500));

        // Panel derecho - datos del pasajero y compra
        JPanel panelDerecho = new JPanel(new BorderLayout(5, 5));

        // Info asiento seleccionado
        JPanel infoAsiento = new JPanel(new GridLayout(2, 1));
        infoAsiento.setBorder(BorderFactory.createTitledBorder("Asiento Seleccionado"));
        lblAsientoSel = new JLabel("Ninguno", SwingConstants.CENTER);
        lblAsientoSel.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrecio = new JLabel("Precio: -", SwingConstants.CENTER);
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 15));
        lblPrecio.setForeground(new Color(0, 120, 50));
        infoAsiento.add(lblAsientoSel);
        infoAsiento.add(lblPrecio);

        // Formulario pasajero
        JPanel formPasajero = new JPanel(new GridBagLayout());
        formPasajero.setBorder(BorderFactory.createTitledBorder("Datos del Pasajero"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdPasajero = new JTextField(12);
        txtNombre = new JTextField(12);
        txtApellido = new JTextField(12);
        txtEmail = new JTextField(12);
        cmbNivelSocio = new JComboBox<>(new String[]{"Regular", "Oro", "Platino"});

        JButton btnBuscarPasajero = new JButton("Buscar");
        btnBuscarPasajero.setBackground(new Color(100, 100, 180));
        btnBuscarPasajero.setForeground(Color.WHITE);

        Object[][] camposPas = {
            {"ID/Cédula:", txtIdPasajero},
            {"", btnBuscarPasajero},
            {"Nombre:", txtNombre},
            {"Apellido:", txtApellido},
            {"Email:", txtEmail},
            {"Nivel Socio:", cmbNivelSocio}
        };

        for (int i = 0; i < camposPas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            formPasajero.add(new JLabel(camposPas[i][0].toString()), gbc);
            gbc.gridx = 1;
            formPasajero.add((Component) camposPas[i][1], gbc);
        }

        // Leyenda
        JPanel leyenda = new JPanel(new GridLayout(4, 1, 2, 2));
        leyenda.setBorder(BorderFactory.createTitledBorder("Leyenda"));
        Color[] cols = {new Color(255,215,0), new Color(100,180,255), new Color(160,220,160), new Color(220,80,80)};
        String[] nombres = {"1ra Clase (+100%)", "Ejecutiva (+50%)", "Económica", "Ocupado"};
        for (int i = 0; i < 4; i++) {
            JLabel lbl = new JLabel("  " + nombres[i]);
            lbl.setOpaque(true);
            lbl.setBackground(cols[i]);
            lbl.setFont(new Font("Arial", Font.PLAIN, 11));
            lbl.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            leyenda.add(lbl);
        }

        btnComprar = new JButton("✔  COMPRAR TIQUETE");
        btnComprar.setBackground(new Color(0, 150, 50));
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setFont(new Font("Arial", Font.BOLD, 14));
        btnComprar.setEnabled(false);

        JPanel botones = new JPanel(new BorderLayout(5, 5));
        botones.add(leyenda, BorderLayout.CENTER);
        botones.add(btnComprar, BorderLayout.SOUTH);

        panelDerecho.add(infoAsiento, BorderLayout.NORTH);
        panelDerecho.add(formPasajero, BorderLayout.CENTER);
        panelDerecho.add(botones, BorderLayout.SOUTH);

        main.add(topPanel, BorderLayout.NORTH);
        main.add(scrollMapa, BorderLayout.CENTER);
        main.add(panelDerecho, BorderLayout.EAST);

        setContentPane(main);

        // Eventos
        btnSeleccionar.addActionListener(e -> cargarMapa());
        btnBuscarPasajero.addActionListener(e -> buscarPasajero());
        btnComprar.addActionListener(e -> comprarTiquete());
    }

    private void cargarVuelos() {
        cmbVuelos.removeAllItems();
        for (int i = 0; i < almacen.getCantVuelos(); i++) {
            cmbVuelos.addItem(almacen.getVuelos()[i].toString());
        }
    }

    private void cargarMapa() {
        if (almacen.getCantVuelos() == 0) {
            JOptionPane.showMessageDialog(this, "No hay vuelos registrados.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idx = cmbVuelos.getSelectedIndex();
        if (idx < 0) return;
        vueloSeleccionado = almacen.getVuelos()[idx];
        asientoSeleccionado = null;
        lblAsientoSel.setText("Ninguno");
        lblPrecio.setText("Precio: -");
        btnComprar.setEnabled(false);

        Avion avion = vueloSeleccionado.getAvion();
        double ocp = avion.getPorcentajeOcupacion();
        lblInfoVuelo.setText(String.format("%s → %s  |  %.0f%% ocupado%s",
                vueloSeleccionado.getOrigen(), vueloSeleccionado.getDestino(), ocp,
                ocp > 80 ? "  ⚠ +20% demanda" : ""));

        dibujarMapa(avion);
    }

    private void dibujarMapa(Avion avion) {
        panelMapa.removeAll();
        int totalFilas = avion.getFilasPrimera() + avion.getFilasEjecutiva() + avion.getFilasEconomica();
        int cols = avion.getColumnas();
        botonesAsientos = new JButton[totalFilas][cols];

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        // Encabezado columnas
        gbc.gridy = 0; gbc.gridx = 0;
        grid.add(new JLabel(""), gbc);
        for (int j = 0; j < cols; j++) {
            gbc.gridx = j + 1;
            JLabel lbl = new JLabel(String.valueOf((char)('A'+j)), SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 11));
            grid.add(lbl, gbc);
        }

        for (int i = 0; i < totalFilas; i++) {
            // Separador de clase
            if (i == avion.getFilasPrimera() || i == avion.getFilasPrimera() + avion.getFilasEjecutiva()) {
                gbc.gridy = i + 1; gbc.gridx = 0; gbc.gridwidth = cols + 1;
                String txt = i == avion.getFilasPrimera() ? "─── CLASE EJECUTIVA ───" : "─── CLASE ECONÓMICA ───";
                JLabel sep = new JLabel(txt, SwingConstants.CENTER);
                sep.setFont(new Font("Arial", Font.BOLD, 10));
                sep.setForeground(Color.DARK_GRAY);
                grid.add(sep, gbc);
                gbc.gridwidth = 1;
                gbc.gridy++;
            }

            // Número de fila
            gbc.gridy = (i < avion.getFilasPrimera() ? i + 1 :
                         i < avion.getFilasPrimera() + avion.getFilasEjecutiva() ? i + 2 : i + 3);
            gbc.gridx = 0;
            JLabel numFila = new JLabel(String.valueOf(i + 1), SwingConstants.RIGHT);
            numFila.setFont(new Font("Arial", Font.PLAIN, 10));
            grid.add(numFila, gbc);

            for (int j = 0; j < cols; j++) {
                gbc.gridx = j + 1;
                Asiento seat = avion.getAsientos()[i][j];
                JButton btn = crearBotonAsiento(seat, i, j);
                botonesAsientos[i][j] = btn;
                grid.add(btn, gbc);
            }
        }

        panelMapa.add(grid);
        panelMapa.revalidate();
        panelMapa.repaint();
    }

    private JButton crearBotonAsiento(Asiento seat, int fi, int co) {
        JButton btn = new JButton(seat.getCodigo());
        btn.setPreferredSize(new Dimension(42, 30));
        btn.setFont(new Font("Arial", Font.BOLD, 9));
        btn.setMargin(new Insets(1, 1, 1, 1));
        actualizarColorBoton(btn, seat);

        if (!seat.isOcupado()) {
            btn.addActionListener(e -> seleccionarAsiento(seat, fi, co));
        }
        return btn;
    }

    private void actualizarColorBoton(JButton btn, Asiento seat) {
        if (seat.isOcupado()) {
            btn.setBackground(new Color(220, 80, 80));
            btn.setEnabled(false);
        } else {
            switch (seat.getZona()) {
                case "Primera":   btn.setBackground(new Color(255, 215, 0)); break;
                case "Ejecutiva": btn.setBackground(new Color(100, 180, 255)); break;
                default:          btn.setBackground(new Color(160, 220, 160));
            }
        }
        btn.setForeground(Color.DARK_GRAY);
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    }

    private void seleccionarAsiento(Asiento seat, int fi, int co) {
        // Deseleccionar anterior
        if (asientoSeleccionado != null) {
            int pf = asientoSeleccionado.getFila();
            int pc = asientoSeleccionado.getColumna();
            if (botonesAsientos != null) {
                actualizarColorBoton(botonesAsientos[pf][pc], asientoSeleccionado);
            }
        }
        asientoSeleccionado = seat;
        botonesAsientos[fi][co].setBackground(new Color(255, 140, 0));
        botonesAsientos[fi][co].setForeground(Color.WHITE);

        double precio = vueloSeleccionado.calcularPrecio(seat.getZona());
        // Descuento si ya tiene pasajero cargado
        Pasajero p = almacen.buscarPasajero(txtIdPasajero.getText().trim());
        if (p != null) precio = p.aplicarDescuento(precio);

        lblAsientoSel.setText("Asiento " + seat.getCodigo() + " | " + seat.getZona());
        lblPrecio.setText(String.format("Precio: $%.2f", precio));
        btnComprar.setEnabled(true);
    }

    private void buscarPasajero() {
        String id = txtIdPasajero.getText().trim();
        if (id.isEmpty()) return;
        Pasajero p = almacen.buscarPasajero(id);
        if (p != null) {
            txtNombre.setText(p.getNombre());
            txtApellido.setText(p.getApellido());
            txtEmail.setText(p.getEmail());
            cmbNivelSocio.setSelectedItem(p.getNivelSocio());
            JOptionPane.showMessageDialog(this, "Pasajero encontrado: " + p.getNombreCompleto(), "Info", JOptionPane.INFORMATION_MESSAGE);
            // Actualizar precio si hay asiento seleccionado
            if (asientoSeleccionado != null) seleccionarAsiento(asientoSeleccionado, asientoSeleccionado.getFila(), asientoSeleccionado.getColumna());
        } else {
            JOptionPane.showMessageDialog(this, "Pasajero no encontrado. Complete los datos para registrarlo.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void comprarTiquete() {
        if (vueloSeleccionado == null || asientoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione vuelo y asiento.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String id = txtIdPasajero.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String email = txtEmail.getText().trim();
        String nivel = (String) cmbNivelSocio.getSelectedItem();

        if (id.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete los datos del pasajero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Registrar o recuperar pasajero
        Pasajero pasajero = almacen.buscarPasajero(id);
        if (pasajero == null) {
            pasajero = new Pasajero(id, nombre, apellido, email, nivel);
            almacen.agregarPasajero(pasajero);
        }

        double precio = vueloSeleccionado.calcularPrecio(asientoSeleccionado.getZona());
        precio = pasajero.aplicarDescuento(precio);

        String msg = String.format(
            "Confirmar compra:\n\nVuelo: %s\nAsiento: %s (%s)\nPasajero: %s\nNivel: %s\nPrecio final: $%.2f",
            vueloSeleccionado.getCodigo(), asientoSeleccionado.getCodigo(),
            asientoSeleccionado.getZona(), pasajero.getNombreCompleto(), nivel, precio);

        int confirm = JOptionPane.showConfirmDialog(this, msg, "Confirmar Compra", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            asientoSeleccionado.ocupar(id);
            Tiquete t = new Tiquete(pasajero, vueloSeleccionado, asientoSeleccionado, precio);
            almacen.agregarTiquete(t);
            JOptionPane.showMessageDialog(this,
                "✔ Tiquete emitido exitosamente!\n\nNúmero: " + t.getNumero() + "\nGuarde este número para futuras gestiones.",
                "Compra Exitosa", JOptionPane.INFORMATION_MESSAGE);
            cargarMapa(); // Refrescar mapa
            limpiarFormPasajero();
        }
    }

    private void limpiarFormPasajero() {
        txtIdPasajero.setText(""); txtNombre.setText("");
        txtApellido.setText(""); txtEmail.setText("");
        cmbNivelSocio.setSelectedIndex(0);
        asientoSeleccionado = null;
        lblAsientoSel.setText("Ninguno");
        lblPrecio.setText("Precio: -");
        btnComprar.setEnabled(false);
    }
}
