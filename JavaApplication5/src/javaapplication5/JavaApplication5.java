/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication5;

import javax.swing.JOptionPane;

/**
 *
 * @author eltro
 */
public class JavaApplication5 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        inicializarAvion();

        int opcion;

        do {
            opcion = Integer.parseInt(JOptionPane.showInputDialog(
                    "1. Ver mapa\n"
                    + "2. Reservar asiento\n"
                    + "3. Reservar con descuento\n"
                    + "4. Consultar pasajero\n"
                    + "5. Ver resumen\n"
                    + "6. Salir"
            ));

            switch (opcion) {
                case 1:
                    mostrarMapa();
                    break;
                case 2:
                    reservar(false);
                    break;
                case 3:
                    reservar(true);
                    break;
                case 4:
                    consultar();
                    break;
                case 5:
                    resumen();
                    break;
            }

        } while (opcion != 6);
    }

    public static void inicializarAvion() {
        String[] letras = {"A", "B", "C", "D"};

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {

                String codigo = (i + 1) + letras[j];

                if (i == 0) {
                    avion[i][j] = new Asiento(codigo, "Primera", 500);
                } else if (i == 1) {
                    avion[i][j] = new Asiento(codigo, "Business", 300);
                } else {
                    avion[i][j] = new Asiento(codigo, "Economica", 100);
                }
            }
        }
    }

    public static void mostrarMapa() {
        String salida = "";

        for (int i = 0; i < 5; i++) {
            salida += "Fila " + (i + 1) + "\n";

            for (int j = 0; j < 4; j++) {
                Asiento a = avion[i][j];
                salida += a.codigo + " - " + a.estado + " - "
                        + a.clase + " - $" + a.precio + "\n";
            }

            salida += "-------------------\n";
        }

        JOptionPane.showMessageDialog(null, salida);
    }

    public static void reservar(boolean descuento) {

        int fila = Integer.parseInt(JOptionPane.showInputDialog("Fila (1-5)")) - 1;
        int col = JOptionPane.showInputDialog("Letra (A-D)").toUpperCase().charAt(0) - 'A';

        Asiento a = avion[fila][col];

        if (!a.estado.equals("Libre")) {
            JOptionPane.showMessageDialog(null, "Asiento no disponible");
            return;
        }

        String nombre = JOptionPane.showInputDialog("Nombre");
        String pasaporte = JOptionPane.showInputDialog("Pasaporte");
        String nacionalidad = JOptionPane.showInputDialog("Nacionalidad");

        Pasajero p = new Pasajero(nombre, pasaporte, nacionalidad);

        double precioFinal = a.precio;

        if (descuento) {
            if (a.clase.equals("Economica")) {
                precioFinal *= 0.9;
            } else if (a.clase.equals("Business")) {
                precioFinal *= 0.85;
            } else {
                precioFinal *= 0.82;
            }
        }

        a.estado = "Ocupado";
        a.pasajero = p;

        JOptionPane.showMessageDialog(null, "Reservado. Precio: $" + precioFinal);
    }

    public static void consultar() {
        int fila = Integer.parseInt(JOptionPane.showInputDialog("Fila (1-5)")) - 1;
        int col = JOptionPane.showInputDialog("Letra (A-D)").toUpperCase().charAt(0) - 'A';

        Asiento a = avion[fila][col];

        if (a.pasajero != null) {
            JOptionPane.showMessageDialog(null,
                    "Nombre: " + a.pasajero.nombre + "\n"
                    + "Pasaporte: " + a.pasajero.pasaporte + "\n"
                    + "Nacionalidad: " + a.pasajero.nacionalidad);
        } else {
            JOptionPane.showMessageDialog(null, "Asiento libre");
        }
    }

    public static void resumen() {
        double total = 0;
        int ocupados = 0;
        int libres = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {

                if (avion[i][j].estado.equals("Ocupado")) {
                    total += avion[i][j].precio;
                    ocupados++;
                } else {
                    libres++;
                }
            }
        }

        double porcentaje = (ocupados * 100.0) / (ocupados + libres);

        JOptionPane.showMessageDialog(null,
                "Total recaudado: $" + total
                + "\nAsientos libres: " + libres
                + "\nOcupación: " + porcentaje + "%");
    }
}
