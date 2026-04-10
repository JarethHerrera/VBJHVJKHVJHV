package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Aerolínea Global-Java");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 520);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        // Panel principal con fondo degradado
        JPanel fondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(10, 30, 80),
                        getWidth(), getHeight(), new Color(0, 100, 170));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        fondo.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(25, 30, 10, 30));

        JLabel titulo = new JLabel("✈  AEROLÍNEA GLOBAL-JAVA", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Sistema de Gestión Aeroportuaria", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitulo.setForeground(new Color(180, 220, 255));

        header.add(titulo, BorderLayout.CENTER);
        header.add(subtitulo, BorderLayout.SOUTH);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(3, 2, 20, 20));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        String[][] modulos = {
            {"✈  Módulo 1", "Administración y Flota"},
            {"🎫  Módulo 2", "Venta de Tiquetes"},
            {"🛍  Módulo 3", "Servicios Adicionales"},
            {"🧳  Módulo 4", "Check-in y Equipaje"},
            {"❌  Módulo 5", "Cancelaciones"},
            {"📊  Reportes", "Estadísticas del Sistema"}
        };

        Color[] colores = {
            new Color(0, 120, 215),
            new Color(0, 150, 100),
            new Color(150, 80, 200),
            new Color(200, 120, 0),
            new Color(200, 50, 50),
            new Color(50, 130, 180)
        };

        for (int i = 0; i < modulos.length; i++) {
            final int idx = i;
            JButton btn = crearBotonModulo(modulos[i][0], modulos[i][1], colores[i]);
            btn.addActionListener(e -> abrirModulo(idx));
            panelBotones.add(btn);
        }

        // Footer
        JLabel footer = new JLabel("SC-202 | Prof. David Núñez Araya | Universidad Fidélitas", SwingConstants.CENTER);
        footer.setFont(new Font("Arial", Font.PLAIN, 11));
        footer.setForeground(new Color(150, 190, 230));
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        fondo.add(header, BorderLayout.NORTH);
        fondo.add(panelBotones, BorderLayout.CENTER);
        fondo.add(footer, BorderLayout.SOUTH);

        setContentPane(fondo);
    }

    private JButton crearBotonModulo(String titulo, String subtitulo, Color color) {
        JButton btn = new JButton("<html><center><b>" + titulo + "</b><br><small>" + subtitulo + "</small></center></html>") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = getModel().isPressed() ? color.darker() :
                             getModel().isRollover() ? color.brighter() : color;
                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 70));
        return btn;
    }

    private void abrirModulo(int idx) {
        switch (idx) {
            case 0: new VentanaFlota().setVisible(true); break;
            case 1: new VentanaVenta().setVisible(true); break;
            case 2: new VentanaServicios().setVisible(true); break;
            case 3: new VentanaCheckin().setVisible(true); break;
            case 4: new VentanaCancelacion().setVisible(true); break;
            case 5: new VentanaReportes().setVisible(true); break;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
