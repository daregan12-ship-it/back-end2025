package mx.tecnm.backend.api.models;

import java.math.BigDecimal;

public class DetalleCarrito {
    private int id;
    private int cantidad;
    private BigDecimal precio; // Precio al momento de agregar
    private int productoId;
    private String nombreProducto; // Dato extra para mostrar en el JSON
    private int usuarioId;

    public DetalleCarrito() {}

    public DetalleCarrito(int id, int cantidad, BigDecimal precio, int productoId, String nombreProducto, int usuarioId) {
        this.id = id;
        this.cantidad = cantidad;
        this.precio = precio;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.usuarioId = usuarioId;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
}