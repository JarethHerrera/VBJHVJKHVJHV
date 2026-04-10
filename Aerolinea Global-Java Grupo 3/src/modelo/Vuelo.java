package modelo;

public class Vuelo {
    private String codigo;
    private Avion avion;
    private String origen;
    private String destino;
    private String fecha;
    private double precioBase;

    public Vuelo(String codigo, Avion avion, String origen, String destino, String fecha, double precioBase) {
        this.codigo = codigo;
        this.avion = avion;
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.precioBase = precioBase;
    }

    public double calcularPrecio(String zona) {
        double precio = precioBase;
        // Ajuste por demanda
        if (avion.getPorcentajeOcupacion() > 80) {
            precio *= 1.20;
        }
        // Ajuste por zona
        switch (zona) {
            case "Primera":
                precio *= 2.0; // +100%
                break;
            case "Ejecutiva":
                precio *= 1.5; // +50%
                break;
        }
        return precio;
    }

    public String getCodigo() { return codigo; }
    public Avion getAvion() { return avion; }
    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public String getFecha() { return fecha; }
    public double getPrecioBase() { return precioBase; }

    @Override
    public String toString() {
        return codigo + " | " + origen + " -> " + destino + " | " + fecha;
    }
}
