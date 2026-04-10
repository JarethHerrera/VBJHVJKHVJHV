package modelo;

public class Producto {
    private String nombre;
    private String categoria; // "DutyFree", "Snack", "Bebida", "Electronica"
    private double precio;
    private static final double IVA = 0.13;

    public Producto(String nombre, String categoria, double precio) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
    }

    public boolean esDutyFree() {
        return categoria.equals("DutyFree") || categoria.equals("Electronica");
    }

    public double getPrecioFinal() {
        if (esDutyFree()) {
            return precio;
        }
        return precio * (1 + IVA);
    }

    public double getImpuesto() {
        if (esDutyFree()) return 0;
        return precio * IVA;
    }

    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public double getPrecio() { return precio; }

    @Override
    public String toString() { return nombre + " (" + categoria + ") - $" + String.format("%.2f", getPrecioFinal()); }
}
