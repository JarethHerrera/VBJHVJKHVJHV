package modelo;

public class Almacen {
    private static Almacen instancia;

    private Avion[] aviones;
    private int cantAviones;

    private Vuelo[] vuelos;
    private int cantVuelos;

    private Pasajero[] pasajeros;
    private int cantPasajeros;

    private Tiquete[] tiquetes;
    private int cantTiquetes;

    private Producto[] catalogo;
    private int cantProductos;

    // Finanzas
    private double ingresosTiquetes;
    private double ingresosAbordo;
    private double ingresosPenalizacionEquipaje;
    private double ingresosPenalizacionCancelacion;

    private Almacen() {
        aviones = new Avion[100];
        vuelos = new Vuelo[200];
        pasajeros = new Pasajero[500];
        tiquetes = new Tiquete[500];
        catalogo = new Producto[50];
        inicializarCatalogo();
    }

    public static Almacen getInstance() {
        if (instancia == null) instancia = new Almacen();
        return instancia;
    }

    private void inicializarCatalogo() {
        catalogo[cantProductos++] = new Producto("Perfume Chanel", "DutyFree", 85.00);
        catalogo[cantProductos++] = new Producto("Perfume Dior", "DutyFree", 95.00);
        catalogo[cantProductos++] = new Producto("Audífonos Bluetooth", "Electronica", 120.00);
        catalogo[cantProductos++] = new Producto("Cargador Universal", "Electronica", 35.00);
        catalogo[cantProductos++] = new Producto("Agua mineral", "Bebida", 3.50);
        catalogo[cantProductos++] = new Producto("Jugo de naranja", "Bebida", 4.00);
        catalogo[cantProductos++] = new Producto("Vino tinto", "Bebida", 8.00);
        catalogo[cantProductos++] = new Producto("Papas fritas", "Snack", 2.50);
        catalogo[cantProductos++] = new Producto("Chocolate", "Snack", 3.00);
        catalogo[cantProductos++] = new Producto("Maní salado", "Snack", 2.00);
    }

    // ---- AVIONES ----
    public void agregarAvion(Avion a) { aviones[cantAviones++] = a; }
    public Avion[] getAviones() { return aviones; }
    public int getCantAviones() { return cantAviones; }
    public Avion buscarAvion(String matricula) {
        for (int i = 0; i < cantAviones; i++)
            if (aviones[i].getMatricula().equalsIgnoreCase(matricula)) return aviones[i];
        return null;
    }

    // ---- VUELOS ----
    public void agregarVuelo(Vuelo v) { vuelos[cantVuelos++] = v; }
    public Vuelo[] getVuelos() { return vuelos; }
    public int getCantVuelos() { return cantVuelos; }
    public Vuelo buscarVuelo(String codigo) {
        for (int i = 0; i < cantVuelos; i++)
            if (vuelos[i].getCodigo().equalsIgnoreCase(codigo)) return vuelos[i];
        return null;
    }

    // ---- PASAJEROS ----
    public void agregarPasajero(Pasajero p) { pasajeros[cantPasajeros++] = p; }
    public Pasajero[] getPasajeros() { return pasajeros; }
    public int getCantPasajeros() { return cantPasajeros; }
    public Pasajero buscarPasajero(String id) {
        for (int i = 0; i < cantPasajeros; i++)
            if (pasajeros[i].getId().equalsIgnoreCase(id)) return pasajeros[i];
        return null;
    }

    // ---- TIQUETES ----
    public void agregarTiquete(Tiquete t) {
        tiquetes[cantTiquetes++] = t;
        ingresosTiquetes += t.getPrecioPagado();
    }
    public Tiquete[] getTiquetes() { return tiquetes; }
    public int getCantTiquetes() { return cantTiquetes; }
    public Tiquete buscarTiquete(String numero) {
        for (int i = 0; i < cantTiquetes; i++)
            if (tiquetes[i].getNumero().equalsIgnoreCase(numero)) return tiquetes[i];
        return null;
    }
    public Tiquete buscarTiquetePorPasajero(String idPasajero) {
        for (int i = 0; i < cantTiquetes; i++)
            if (tiquetes[i].getPasajero().getId().equalsIgnoreCase(idPasajero)
                    && tiquetes[i].getEstado().equals("Activo")) return tiquetes[i];
        return null;
    }

    // ---- CATALOGO ----
    public Producto[] getCatalogo() { return catalogo; }
    public int getCantProductos() { return cantProductos; }

    // ---- FINANZAS ----
    public void sumarIngresosAbordo(double monto) { ingresosAbordo += monto; }
    public void sumarPenalizacionEquipaje(double monto) { ingresosPenalizacionEquipaje += monto; }
    public void sumarPenalizacionCancelacion(double monto) { ingresosPenalizacionCancelacion += monto; }
    public double getIngresosTiquetes() { return ingresosTiquetes; }
    public double getIngresosAbordo() { return ingresosAbordo; }
    public double getIngresosPenalizacionEquipaje() { return ingresosPenalizacionEquipaje; }
    public double getIngresosPenalizacionCancelacion() { return ingresosPenalizacionCancelacion; }
    public double getTotalIngresos() {
        return ingresosTiquetes + ingresosAbordo + ingresosPenalizacionEquipaje + ingresosPenalizacionCancelacion;
    }
}
