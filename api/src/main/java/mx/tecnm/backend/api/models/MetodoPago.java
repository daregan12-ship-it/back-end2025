package mx.tecnm.backend.api.models;

public class MetodoPago {
    private int id;
    private String nombre;
    private double comision;
    private boolean activo; // Campo para Soft Delete

    public MetodoPago() {}

    public MetodoPago(int id, String nombre, double comision, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.comision = comision;
        this.activo = activo;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getComision() { return comision; }
    public void setComision(double comision) { this.comision = comision; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}