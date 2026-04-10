package vista;

import modelo.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaServicios extends JFrame {

    private Almacen almacen = Almacen.getInstance();
    private Tiquete tiqueteActual;

    private JTextField txtBuscarTiquete;
    private JLabel lblInfoTiquete;
    private JComboBox<String> cmbMenu;
    private JButton btnGuardarMenu;

    // Carrito
    private DefaultTableModel modeloCarrito;
    private JLabel lblSubtotal, lblImpuestos, lblTotal;
    private JList<String> listaCatalogo;

    public VentanaServicios() {
        setTitle("Módulo 3 - Servicios Adicionales");
        setSize(900, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel superior búsqueda
        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        busqueda.setBorder(BorderFactory.createTitledBorder("Buscar Reserva"));
        txtBuscarTiquete = new JTextField(12);
        JButton btnBuscar = new JButton("Buscar Tiquete");
        btnBuscar.setBackground(new Color(0, 120, 215));
        btnBuscar.setForeground(Color.WHITE);
        lblInfoTiquete = new JLabel("Ingrese el número de tiquete (Ej: TK1001)");
        lblInfoTiquete.setFont(new Font("Arial", Font.ITALIC, 12));
        busqueda.add(new JLabel("Nº Tiquete:"));
        busqueda.add(txtBuscarTiquete);
        busqueda.add(btnBuscar);
        busqueda.add(lblInfoTiquete);

        // Panel de tabs de servicios
        JTabbedPane tabs = new JTabbedPane();

        // Tab menú especial
        tabs.addTab("🍽 Menú Especial", crearTabMenu());

        // Tab carrito a bordo
        tabs.addTab("🛍 Duty Free / Snacks", crearTabCarrito());

        main.add(busqueda, BorderLayout.NORTH);
        main.add(tabs, BorderLayout.CENTER);
        setContentPane(main);

        btnBuscar.addActionListener(e -> buscarTiquete());
    }

    private JPanel crearTabMenu() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel icono = new JLabel("🍽", SwingConstants.CENTER);
        icono.setFont(new Font("Arial", Font.PLAIN, 60));

        JLabel lblDesc = new JLabel("<html><center>Seleccione el tipo de menú especial para este pasajero.<br>"
                + "Esta preferencia quedará registrada en el tiquete para la tripulación.</center></html>",
                SwingConstants.CENTER);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));

        cmbMenu = new JComboBox<>(new String[]{"Estándar", "Vegetariano", "Kosher", "Sin Gluten"});
        cmbMenu.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbMenu.setPreferredSize(new Dimension(220, 35));

        btnGuardarMenu = new JButton("Guardar Preferencia");
        btnGuardarMenu.setBackground(new Color(0, 150, 100));
        btnGuardarMenu.setForeground(Color.WHITE);
        btnGuardarMenu.setFont(new Font("Arial", Font.BOLD, 13));
        btnGuardarMenu.setEnabled(false);

        gbc.gridy = 0; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(icono, gbc);
        gbc.gridy = 1;
        panel.add(lblDesc, gbc);
        gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(new JLabel("Tipo de Menú:"), gbc);
        gbc.gridx = 1;
        panel.add(cmbMenu, gbc);
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(btnGuardarMenu, gbc);

        btnGuardarMenu.addActionListener(e -> guardarMenu());
        return panel;
    }

    private JPanel crearTabCarrito() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Catálogo
        JPanel panelCatalogo = new JPanel(new BorderLayout());
        panelCatalogo.setBorder(BorderFactory.createTitledBorder("Catálogo de Productos"));

        DefaultListModel<String> modeloCat = new DefaultListModel<>();
        for (int i = 0; i < almacen.getCantProductos(); i++) {
            Producto p = almacen.getCatalogo()[i];
            modeloCat.addElement(String.format("%-25s  %-10s  $%.2f%s",
                    p.getNombre(), p.getCategoria(), p.getPrecioFinal(),
                    p.esDutyFree() ? " (Libre impuesto)" : " (+ IVA 13%)"));
        }
        listaCatalogo = new JList<>(modeloCat);
        listaCatalogo.setFont(new Font("Monospaced", Font.PLAIN, 11));
        listaCatalogo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelCatalogo.add(new JScrollPane(listaCatalogo));
        panelCatalogo.setPreferredSize(new Dimension(400, 200));

        JButton btnAgregar = new JButton("Agregar al Carrito >");
        btnAgregar.setBackground(new Color(0, 120, 215));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 12));

        // Carrito
        JPanel panelCarrito = new JPanel(new BorderLayout());
        panelCarrito.setBorder(BorderFactory.createTitledBorder("Carrito"));

        String[] columnas = {"Producto", "Categoría", "Precio Base", "Impuesto", "Total"};
        modeloCarrito = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaCarrito.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        panelCarrito.add(new JScrollPane(tablaCarrito));

        // Totales
        JPanel totales = new JPanel(new GridLayout(3, 2, 5, 3));
        totales.setBorder(BorderFactory.createTitledBorder("Resumen de Factura"));
        lblSubtotal = new JLabel("$0.00"); lblSubtotal.setFont(new Font("Arial", Font.PLAIN, 13));
        lblImpuestos = new JLabel("$0.00"); lblImpuestos.setFont(new Font("Arial", Font.PLAIN, 13));
        lblTotal = new JLabel("$0.00"); lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotal.setForeground(new Color(0, 120, 50));
        totales.add(new JLabel("Subtotal:")); totales.add(lblSubtotal);
        totales.add(new JLabel("IVA (13%):")); totales.add(lblImpuestos);
        totales.add(new JLabel("TOTAL:")); totales.add(lblTotal);

        JButton btnGenerarFactura = new JButton("Generar Factura a Bordo");
        btnGenerarFactura.setBackground(new Color(200, 100, 0));
        btnGenerarFactura.setForeground(Color.WHITE);
        btnGenerarFactura.setFont(new Font("Arial", Font.BOLD, 13));

        JPanel sur = new JPanel(new BorderLayout(5, 5));
        sur.add(totales, BorderLayout.CENTER);
        sur.add(btnGenerarFactura, BorderLayout.SOUTH);

        JPanel centroCarrito = new JPanel(new BorderLayout());
        centroCarrito.add(panelCarrito, BorderLayout.CENTER);
        centroCarrito.add(sur, BorderLayout.SOUTH);

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.add(panelCatalogo, BorderLayout.NORTH);
        centro.add(new JPanel() {{ setOpaque(false); add(btnAgregar); }}, BorderLayout.CENTER);
        centro.add(centroCarrito, BorderLayout.SOUTH);

        panel.add(centro);

        btnAgregar.addActionListener(e -> agregarAlCarrito());
        btnGenerarFactura.addActionListener(e -> generarFactura());

        return panel;
    }

    private void buscarTiquete() {
        String num = txtBuscarTiquete.getText().trim().toUpperCase();
        tiqueteActual = almacen.buscarTiquete(num);
        if (tiqueteActual == null) {
            lblInfoTiquete.setText("❌ Tiquete no encontrado.");
            lblInfoTiquete.setForeground(Color.RED);
            cmbMenu.setEnabled(false);
            btnGuardarMenu.setEnabled(false);
        } else {
            lblInfoTiquete.setText("✔ " + tiqueteActual.getPasajero().getNombreCompleto()
                    + " | Vuelo: " + tiqueteActual.getVuelo().getCodigo()
                    + " | Asiento: " + tiqueteActual.getAsiento().getCodigo());
            lblInfoTiquete.setForeground(new Color(0, 100, 0));
            cmbMenu.setEnabled(true);
            btnGuardarMenu.setEnabled(true);
            cmbMenu.setSelectedItem(tiqueteActual.getMenuEspecial());
        }
    }

    private void guardarMenu() {
        if (tiqueteActual == null) return;
        String menu = (String) cmbMenu.getSelectedItem();
        tiqueteActual.setMenuEspecial(menu);
        JOptionPane.showMessageDialog(this,
                "Preferencia de menú guardada: " + menu + "\nPara el tiquete: " + tiqueteActual.getNumero(),
                "Guardado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void agregarAlCarrito() {
        if (tiqueteActual == null) {
            JOptionPane.showMessageDialog(this, "Primero busque y cargue un tiquete válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idx = listaCatalogo.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto del catálogo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Producto p = almacen.getCatalogo()[idx];
        tiqueteActual.agregarProducto(p);

        modeloCarrito.addRow(new Object[]{
            p.getNombre(),
            p.getCategoria(),
            String.format("$%.2f", p.getPrecio()),
            String.format("$%.2f", p.getImpuesto()),
            String.format("$%.2f", p.getPrecioFinal())
        });
        actualizarTotales();
    }

    private void actualizarTotales() {
        if (tiqueteActual == null) return;
        double subtotal = 0, impuestos = 0, total = 0;
        for (int i = 0; i < tiqueteActual.getCantProductos(); i++) {
            Producto p = tiqueteActual.getProductosAbordo()[i];
            subtotal += p.getPrecio();
            impuestos += p.getImpuesto();
            total += p.getPrecioFinal();
        }
        lblSubtotal.setText(String.format("$%.2f", subtotal));
        lblImpuestos.setText(String.format("$%.2f", impuestos));
        lblTotal.setText(String.format("$%.2f", total));
    }

    private void generarFactura() {
        if (tiqueteActual == null || tiqueteActual.getCantProductos() == 0) {
            JOptionPane.showMessageDialog(this, "No hay productos en el carrito.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("════════════════════════════════════\n");
        sb.append("    FACTURA A BORDO - AEROLÍNEA GLOBAL\n");
        sb.append("════════════════════════════════════\n");
        sb.append("Tiquete : ").append(tiqueteActual.getNumero()).append("\n");
        sb.append("Pasajero: ").append(tiqueteActual.getPasajero().getNombreCompleto()).append("\n");
        sb.append("Vuelo   : ").append(tiqueteActual.getVuelo().getCodigo()).append("\n");
        sb.append("Asiento : ").append(tiqueteActual.getAsiento().getCodigo()).append("\n");
        sb.append("────────────────────────────────────\n");
        sb.append(String.format("%-22s %6s %7s %8s\n", "Producto", "Precio", "IVA", "Total"));
        sb.append("────────────────────────────────────\n");

        double subtotal = 0, impuestos = 0, total = 0;
        for (int i = 0; i < tiqueteActual.getCantProductos(); i++) {
            Producto p = tiqueteActual.getProductosAbordo()[i];
            sb.append(String.format("%-22s %6.2f %7.2f %8.2f\n",
                    p.getNombre(), p.getPrecio(), p.getImpuesto(), p.getPrecioFinal()));
            subtotal += p.getPrecio();
            impuestos += p.getImpuesto();
            total += p.getPrecioFinal();
        }
        sb.append("────────────────────────────────────\n");
        sb.append(String.format("%-22s %6.2f %7.2f %8.2f\n", "TOTAL", subtotal, impuestos, total));
        sb.append("════════════════════════════════════\n");
        sb.append("¡Gracias por volar con nosotros!\n");

        almacen.sumarIngresosAbordo(total);

        JTextArea area = new JTextArea(sb.toString());
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Factura a Bordo", JOptionPane.PLAIN_MESSAGE);
    }
}
