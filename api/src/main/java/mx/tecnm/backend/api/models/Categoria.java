package mx.tecnm.backend.api.models;

public class Categoria {
    private int id;
    private String nombre;
    private boolean activo; // Este es el campo nuevo para el "Borrado Lógico"

    // Constructor vacío (necesario para Spring/RowMapper)
    public Categoria() {
    }

    // Constructor con datos
    public Categoria(int id, String nombre, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
    }

    // --- GETTERS Y SETTERS (Solo una vez cada uno) ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter especial para booleanos (se suele llamar "isAlgo")
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}