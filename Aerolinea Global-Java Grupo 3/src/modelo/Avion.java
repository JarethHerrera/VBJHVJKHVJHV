package modelo;

public class Avion {
    private String matricula;
    private String modelo;
    private int filasPrimera;
    private int filasEjecutiva;
    private int filasEconomica;
    private int columnas;
    private Asiento[][] asientos;

    public Avion(String matricula, String modelo, int filasPrimera, int filasEjecutiva, int filasEconomica, int columnas) {
        this.matricula = matricula;
        this.modelo = modelo;
        this.filasPrimera = filasPrimera;
        this.filasEjecutiva = filasEjecutiva;
        this.filasEconomica = filasEconomica;
        this.columnas = columnas;
        inicializarAsientos();
    }

    private void inicializarAsientos() {
        int totalFilas = filasPrimera + filasEjecutiva + filasEconomica;
        asientos = new Asiento[totalFilas][columnas];
        for (int i = 0; i < totalFilas; i++) {
            for (int j = 0; j < columnas; j++) {
                String zona;
                if (i < filasPrimera) {
                    zona = "Primera";
                } else if (i < filasPrimera + filasEjecutiva) {
                    zona = "Ejecutiva";
                } else {
                    zona = "Economica";
                }
                asientos[i][j] = new Asiento(i, j, zona);
            }
        }
    }

    public int getTotalAsientos() {
        return (filasPrimera + filasEjecutiva + filasEconomica) * columnas;
    }

    public int getAsientosOcupados() {
        int count = 0;
        for (Asiento[] fila : asientos) {
            for (Asiento a : fila) {
                if (a.isOcupado()) count++;
            }
        }
        return count;
    }

    public double getPorcentajeOcupacion() {
        return (double) getAsientosOcupados() / getTotalAsientos() * 100;
    }

    public String getMatricula() { return matricula; }
    public String getModelo() { return modelo; }
    public int getFilasPrimera() { return filasPrimera; }
    public int getFilasEjecutiva() { return filasEjecutiva; }
    public int getFilasEconomica() { return filasEconomica; }
    public int getColumnas() { return columnas; }
    public Asiento[][] getAsientos() { return asientos; }

    @Override
    public String toString() { return matricula + " - " + modelo; }
}
