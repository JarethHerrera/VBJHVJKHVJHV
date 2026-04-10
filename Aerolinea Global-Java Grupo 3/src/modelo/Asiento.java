package modelo;

public class Asiento {
    private int fila;
    private int columna;
    private String zona; // "Primera", "Ejecutiva", "Economica"
    private boolean ocupado;
    private String pasajeroId;

    public Asiento(int fila, int columna, String zona) {
        this.fila = fila;
        this.columna = columna;
        this.zona = zona;
        this.ocupado = false;
        this.pasajeroId = null;
    }

    public void ocupar(String pasajeroId) {
        this.ocupado = true;
        this.pasajeroId = pasajeroId;
    }

    public void liberar() {
        this.ocupado = false;
        this.pasajeroId = null;
    }

    public String getCodigo() {
        char col = (char) ('A' + columna);
        return (fila + 1) + "" + col;
    }

    public int getFila() { return fila; }
    public int getColumna() { return columna; }
    public String getZona() { return zona; }
    public boolean isOcupado() { return ocupado; }
    public String getPasajeroId() { return pasajeroId; }
}
