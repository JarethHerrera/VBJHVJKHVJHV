package modelo;

public class Pasajero {
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String nivelSocio; // "Platino", "Oro", "Regular"

    public Pasajero(String id, String nombre, String apellido, String email, String nivelSocio) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.nivelSocio = nivelSocio;
    }

    public double aplicarDescuento(double precio) {
        switch (nivelSocio) {
            case "Platino": return precio * 0.90;
            case "Oro":     return precio * 0.95;
            default:        return precio;
        }
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public String getNivelSocio() { return nivelSocio; }
    public String getNombreCompleto() { return nombre + " " + apellido; }

    @Override
    public String toString() { return id + " - " + getNombreCompleto(); }
}
