package mx.tecnm.backend.api.models;

import java.math.BigDecimal;
import java.sql.Timestamp; // Usamos Timestamp para fecha y hora exacta

public class Pedido {
    private int id;
    private Timestamp fecha;
    private String numero; // UUID
    private BigDecimal importeProductos;
    private BigDecimal importeEnvio;
    private int usuarioId;
    private int metodoPagoId;
    private Timestamp fechaHoraPago;
    private BigDecimal importeIva; // Generado por BD
    private BigDecimal total;      // Generado por BD

    public Pedido() {}

    // Constructor completo
    public Pedido(int id, Timestamp fecha, String numero, BigDecimal importeProductos, 
                  BigDecimal importeEnvio, int usuarioId, int metodoPagoId, 
                  Timestamp fechaHoraPago, BigDecimal importeIva, BigDecimal total) {
        this.id = id;
        this.fecha = fecha;
        this.numero = numero;
        this.importeProductos = importeProductos;
        this.importeEnvio = importeEnvio;
        this.usuarioId = usuarioId;
        this.metodoPagoId = metodoPagoId;
        this.fechaHoraPago = fechaHoraPago;
        this.importeIva = importeIva;
        this.total = total;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public BigDecimal getImporteProductos() { return importeProductos; }
    public void setImporteProductos(BigDecimal importeProductos) { this.importeProductos = importeProductos; }

    public BigDecimal getImporteEnvio() { return importeEnvio; }
    public void setImporteEnvio(BigDecimal importeEnvio) { this.importeEnvio = importeEnvio; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getMetodoPagoId() { return metodoPagoId; }
    public void setMetodoPagoId(int metodoPagoId) { this.metodoPagoId = metodoPagoId; }

    public Timestamp getFechaHoraPago() { return fechaHoraPago; }
    public void setFechaHoraPago(Timestamp fechaHoraPago) { this.fechaHoraPago = fechaHoraPago; }

    public BigDecimal getImporteIva() { return importeIva; }
    public void setImporteIva(BigDecimal importeIva) { this.importeIva = importeIva; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}