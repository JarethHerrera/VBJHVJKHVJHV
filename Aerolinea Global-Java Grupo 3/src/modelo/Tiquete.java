package modelo;

public class Tiquete {
    private static int contador = 1000;
    private String numero;
    private Pasajero pasajero;
    private Vuelo vuelo;
    private Asiento asiento;
    private double precioPagado;
    private String menuEspecial; // Mod 3
    private Producto[] productosAbordo; // Mod 3
    private int cantProductos;
    private String estado; // "Activo", "Cancelado", "Check-in"

    public Tiquete(Pasajero pasajero, Vuelo vuelo, Asiento asiento, double precioPagado) {
        this.numero = "TK" + (++contador);
        this.pasajero = pasajero;
        this.vuelo = vuelo;
        this.asiento = asiento;
        this.precioPagado = precioPagado;
        this.menuEspecial = "Estándar";
        this.productosAbordo = new Producto[50];
        this.cantProductos = 0;
        this.estado = "Activo";
    }

    public void agregarProducto(Producto p) {
        if (cantProductos < productosAbordo.length) {
            productosAbordo[cantProductos++] = p;
        }
    }

    public double getTotalProductos() {
        double total = 0;
        for (int i = 0; i < cantProductos; i++) {
            total += productosAbordo[i].getPrecioFinal();
        }
        return total;
    }

    public String getNumero() { return numero; }
    public Pasajero getPasajero() { return pasajero; }
    public Vuelo getVuelo() { return vuelo; }
    public Asiento getAsiento() { return asiento; }
    public double getPrecioPagado() { return precioPagado; }
    public String getMenuEspecial() { return menuEspecial; }
    public void setMenuEspecial(String menu) { this.menuEspecial = menu; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Producto[] getProductosAbordo() { return productosAbordo; }
    public int getCantProductos() { return cantProductos; }

    @Override
    public String toString() {
        return numero + " | " + pasajero.getNombreCompleto() + " | " + vuelo.getCodigo() + " | Asiento: " + asiento.getCodigo();
    }
}
