/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication5;

/**
 *
 * @author eltro
 */
    public class Asiento {
    String codigo;
    String clase;
    double precio;
    String estado; // Libre, Ocupado, Reservado
    Pasajero pasajero;

    public Asiento(String codigo, String clase, double precio) {
        this.codigo = codigo;
        this.clase = clase;
        this.precio = precio;
        this.estado = "Libre";
        this.pasajero = null;
    }
}

