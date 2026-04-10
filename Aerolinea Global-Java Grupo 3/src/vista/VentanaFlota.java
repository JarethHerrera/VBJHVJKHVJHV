package vista;

import modelo.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaFlota extends JFrame {

    private Almacen almacen = Almacen.getInstance();
    private JTabbedPane tabs;

    // Tab Aviones
    private JTextField txtMatricula, txtModelo, txtFilasPrimera, txtFilasEjec, txtFilasEcon, txtColumnas;
    private JPanel panelDibujo;
    private JList<String> listaAviones;
    private DefaultListModel<String> modeloLista;

    // Tab Vuelos
    private JTextField txtCodigoVuelo, txtOrigen, txtDestino, txtFecha, txtPrecioBase;
    private JComboBox<String> cmbAviones;
    private JList<String> listaVuelos;
    private DefaultListModel<String> modeloListaVuelos;

    public VentanaFlota() {
        setTitle("Módulo 1 - Administración y Flota");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 13));
        tabs.addTab("✈ Aviones", crearTabAviones());
        tabs.addTab("🗓 Vuelos", crearTabVuelos());
        add(tabs);
    }

    private JPanel crearTabAviones() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Formulario
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Registrar Avión"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMatricula = new JTextField(10);
        txtModelo = new JTextField(10);
        txtFilasPrimera = new JTextField("2", 5);
        txtFilasEjec = new JTextField("3", 5);
        txtFilasEcon = new JTextField("10", 5);
        txtColumnas = new JTextField("6", 5);

        String[] labels = {"Matrícula:", "Modelo:", "Filas 1ra Clase:", "Filas Ejecutiva:", "Filas Económica:", "Columnas:"};
        JTextField[] campos = {txtMatricula, txtModelo, txtFilasPrimera, txtFilasEjec, txtFilasEcon, txtColumnas};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            form.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            form.add(campos[i], gbc);
        }

        JButton btnRegistrar = new JButton("Registrar Avión");
        btnRegistrar.setBackground(new Color(0, 120, 215));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        form.add(btnRegistrar, gbc);

        JButton btnDibujar = new JButton("Ver Distribución");
        btnDibujar.setBackground(new Color(0, 150, 100));
        btnDibujar.setForeground(Color.WHITE);
        gbc.gridy = labels.length + 1;
        form.add(btnDibujar, gbc);

        // Lista de aviones
        modeloLista = new DefaultListModel<>();
        listaAviones = new JList<>(modeloLista);
        listaAviones.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollLista = new JScrollPane(listaAviones);
        scrollLista.setBorder(BorderFactory.createTitledBorder("Flota Registrada"));
        scrollLista.setPreferredSize(new Dimension(250, 200));

        // Panel de dibujo del avión
        panelDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(245, 245, 245));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.GRAY);
                g.setFont(new Font("Arial", Font.ITALIC, 12));
                g.drawString("Seleccione un avión y pulse 'Ver Distribución'", 20, getHeight() / 2);
            }
        };
        panelDibujo.setPreferredSize(new Dimension(400, 300));
        panelDibujo.setBorder(BorderFactory.createTitledBorder("Distribución del Avión"));

        JPanel izquierdo = new JPanel(new BorderLayout(5, 5));
        izquierdo.add(form, BorderLayout.NORTH);
        izquierdo.add(scrollLista, BorderLayout.CENTER);

        panel.add(izquierdo, BorderLayout.WEST);
        panel.add(panelDibujo, BorderLayout.CENTER);

        // Eventos
        btnRegistrar.addActionListener(e -> registrarAvion());
        btnDibujar.addActionListener(e -> dibujarAvionSeleccionado());

        return panel;
    }

    private JPanel crearTabVuelos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Crear Vuelo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCodigoVuelo = new JTextField(10);
        txtOrigen = new JTextField(10);
        txtDestino = new JTextField(10);
        txtFecha = new JTextField("2025-12-25", 10);
        txtPrecioBase = new JTextField("200.00", 10);
        cmbAviones = new JComboBox<>();

        Object[][] filas = {
            {"Código Vuelo:", txtCodigoVuelo},
            {"Avión:", cmbAviones},
            {"Origen:", txtOrigen},
            {"Destino:", txtDestino},
            {"Fecha (AAAA-MM-DD):", txtFecha},
            {"Precio Base ($):", txtPrecioBase}
        };

        for (int i = 0; i < filas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            form.add(new JLabel(filas[i][0].toString()), gbc);
            gbc.gridx = 1;
            form.add((Component) filas[i][1], gbc);
        }

        JButton btnCrear = new JButton("Crear Vuelo");
        btnCrear.setBackground(new Color(0, 120, 215));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = filas.length; gbc.gridwidth = 2;
        form.add(btnCrear, gbc);

        modeloListaVuelos = new DefaultListModel<>();
        listaVuelos = new JList<>(modeloListaVuelos);
        listaVuelos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(listaVuelos);
        scroll.setBorder(BorderFactory.createTitledBorder("Vuelos Registrados"));

        panel.add(form, BorderLayout.WEST);
        panel.add(scroll, BorderLayout.CENTER);

        btnCrear.addActionListener(e -> crearVuelo());
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 1) actualizarComboAviones();
        });

        return panel;
    }

    private void registrarAvion() {
        try {
            String mat = txtMatricula.getText().trim().toUpperCase();
            String mod = txtModelo.getText().trim();
            if (mat.isEmpty() || mod.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete matrícula y modelo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (almacen.buscarAvion(mat) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un avión con esa matrícula.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int fp = Integer.parseInt(txtFilasPrimera.getText().trim());
            int fe = Integer.parseInt(txtFilasEjec.getText().trim());
            int fec = Integer.parseInt(txtFilasEcon.getText().trim());
            int col = Integer.parseInt(txtColumnas.getText().trim());
            if (fp < 1 || fe < 1 || fec < 1 || col < 2 || col > 10) {
                JOptionPane.showMessageDialog(this, "Valores de filas/columnas inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Avion a = new Avion(mat, mod, fp, fe, fec, col);
            almacen.agregarAvion(a);
            modeloLista.addElement(a.toString() + " [" + a.getTotalAsientos() + " asientos]");
            JOptionPane.showMessageDialog(this, "Avión registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormAvion();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void dibujarAvionSeleccionado() {
        int idx = listaAviones.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un avión de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Avion avion = almacen.getAviones()[idx];
        dibujarAvion(avion);
    }

    private void dibujarAvion(Avion avion) {
        panelDibujo.removeAll();
        panelDibujo.setLayout(new BorderLayout());

        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Asiento[][] seats = avion.getAsientos();
                int totalFilas = avion.getFilasPrimera() + avion.getFilasEjecutiva() + avion.getFilasEconomica();
                int cols = avion.getColumnas();
                int cellW = Math.min(40, (getWidth() - 80) / cols);
                int cellH = 28;
                int startX = (getWidth() - cols * (cellW + 4)) / 2;
                int startY = 60;

                // Dibujar nariz del avión
                int cx = getWidth() / 2;
                g2.setColor(new Color(100, 140, 200));
                int[] px = {cx - 30, cx + 30, cx};
                int[] py = {startY - 25, startY - 25, startY - 50};
                g2.fillPolygon(px, py, 3);

                // Leyenda
                int lx = 10;
                dibujarLeyenda(g2, lx, 10);

                // Asientos
                for (int i = 0; i < totalFilas; i++) {
                    // Separador de zona
                    if (i == avion.getFilasPrimera()) {
                        g2.setColor(new Color(0, 120, 215));
                        g2.setFont(new Font("Arial", Font.BOLD, 9));
                        g2.drawString("── CLASE EJECUTIVA ──", startX, startY + i * (cellH + 4) - 4);
                    } else if (i == avion.getFilasPrimera() + avion.getFilasEjecutiva()) {
                        g2.setColor(new Color(80, 160, 80));
                        g2.setFont(new Font("Arial", Font.BOLD, 9));
                        g2.drawString("── CLASE ECONÓMICA ──", startX, startY + i * (cellH + 4) - 4);
                    }

                    // Número de fila
                    g2.setColor(Color.DARK_GRAY);
                    g2.setFont(new Font("Arial", Font.PLAIN, 10));
                    g2.drawString(String.valueOf(i + 1), startX - 18, startY + i * (cellH + 4) + cellH - 8);

                    for (int j = 0; j < cols; j++) {
                        int x = startX + j * (cellW + 4);
                        int y = startY + i * (cellH + 4);

                        Asiento seat = seats[i][j];
                        Color fill;
                        if (seat.isOcupado()) {
                            fill = new Color(220, 80, 80);
                        } else {
                            switch (seat.getZona()) {
                                case "Primera":   fill = new Color(255, 215, 0); break;
                                case "Ejecutiva": fill = new Color(100, 180, 255); break;
                                default:          fill = new Color(160, 220, 160);
                            }
                        }

                        g2.setColor(fill);
                        g2.fillRoundRect(x, y, cellW, cellH, 6, 6);
                        g2.setColor(Color.DARK_GRAY);
                        g2.drawRoundRect(x, y, cellW, cellH, 6, 6);
                        g2.setFont(new Font("Arial", Font.BOLD, 9));
                        g2.drawString(seat.getCodigo(), x + 3, y + 16);
                    }
                }

                // Cola del avión
                g2.setColor(new Color(100, 140, 200));
                int lastY = startY + totalFilas * (cellH + 4) + 5;
                g2.fillRect(cx - 30, lastY, 60, 12);
                int[] px2 = {cx - 60, cx + 60, cx};
                int[] py2 = {lastY + 12, lastY + 12, lastY + 35};
                g2.fillPolygon(px2, py2, 3);
            }

            private void dibujarLeyenda(Graphics2D g2, int x, int y) {
                g2.setFont(new Font("Arial", Font.BOLD, 10));
                g2.setColor(Color.DARK_GRAY);
                g2.drawString("Leyenda:", x, y + 12);
                Color[] cols2 = {new Color(255,215,0), new Color(100,180,255), new Color(160,220,160), new Color(220,80,80)};
                String[] nombres = {"1ra Clase", "Ejecutiva", "Económica", "Ocupado"};
                for (int i = 0; i < 4; i++) {
                    g2.setColor(cols2[i]);
                    g2.fillRoundRect(x, y + 18 + i * 18, 14, 12, 4, 4);
                    g2.setColor(Color.DARK_GRAY);
                    g2.drawRoundRect(x, y + 18 + i * 18, 14, 12, 4, 4);
                    g2.setFont(new Font("Arial", Font.PLAIN, 9));
                    g2.drawString(nombres[i], x + 18, y + 28 + i * 18);
                }
            }
        };

        JLabel lblTitulo = new JLabel(avion.getMatricula() + " - " + avion.getModelo()
                + "  |  " + avion.getTotalAsientos() + " asientos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 13));
        lblTitulo.setBorder(new EmptyBorder(5, 5, 5, 5));

        panelDibujo.add(lblTitulo, BorderLayout.NORTH);
        panelDibujo.add(new JScrollPane(canvas), BorderLayout.CENTER);

        panelDibujo.revalidate();
        panelDibujo.repaint();
    }

    private void crearVuelo() {
        try {
            String cod = txtCodigoVuelo.getText().trim().toUpperCase();
            String origen = txtOrigen.getText().trim();
            String destino = txtDestino.getText().trim();
            String fecha = txtFecha.getText().trim();
            double precio = Double.parseDouble(txtPrecioBase.getText().trim());

            if (cod.isEmpty() || origen.isEmpty() || destino.isEmpty() || fecha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (almacen.buscarVuelo(cod) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un vuelo con ese código.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (cmbAviones.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un avión.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Avion avion = almacen.getAviones()[cmbAviones.getSelectedIndex()];
            Vuelo v = new Vuelo(cod, avion, origen, destino, fecha, precio);
            almacen.agregarVuelo(v);
            modeloListaVuelos.addElement(v.toString() + " | $" + precio);
            JOptionPane.showMessageDialog(this, "Vuelo creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormVuelo();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarComboAviones() {
        cmbAviones.removeAllItems();
        for (int i = 0; i < almacen.getCantAviones(); i++) {
            cmbAviones.addItem(almacen.getAviones()[i].toString());
        }
    }

    private void cargarDatos() {
        for (int i = 0; i < almacen.getCantAviones(); i++) {
            Avion a = almacen.getAviones()[i];
            modeloLista.addElement(a.toString() + " [" + a.getTotalAsientos() + " asientos]");
        }
        for (int i = 0; i < almacen.getCantVuelos(); i++) {
            Vuelo v = almacen.getVuelos()[i];
            modeloListaVuelos.addElement(v.toString() + " | $" + v.getPrecioBase());
        }
    }

    private void limpiarFormAvion() {
        txtMatricula.setText(""); txtModelo.setText("");
        txtFilasPrimera.setText("2"); txtFilasEjec.setText("3");
        txtFilasEcon.setText("10"); txtColumnas.setText("6");
    }

    private void limpiarFormVuelo() {
        txtCodigoVuelo.setText(""); txtOrigen.setText("");
        txtDestino.setText(""); txtFecha.setText("2025-12-25");
        txtPrecioBase.setText("200.00");
    }
}
