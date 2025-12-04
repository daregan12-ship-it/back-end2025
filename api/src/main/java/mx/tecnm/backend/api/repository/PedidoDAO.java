package mx.tecnm.backend.api.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import mx.tecnm.backend.api.models.Pedido;
import mx.tecnm.backend.api.models.Producto;

@Repository
public class PedidoDAO {

    @Autowired
    private JdbcClient jdbcClient;

    // 1. LISTAR PEDIDOS DE UN USUARIO (Historial)
    public List<Pedido> obtenerPedidosPorUsuario(int usuarioId) {
        String sql = "SELECT * FROM pedidos WHERE usuarios_id = ? ORDER BY fecha DESC";
        return jdbcClient.sql(sql).param(usuarioId).query(new PedidoRM()).list();
    }

    // 2. OBTENER UN PEDIDO POR ID
    public Pedido obtenerPedidoPorId(int id) {
        String sql = "SELECT * FROM pedidos WHERE id = ?";
        return jdbcClient.sql(sql).param(id).query(new PedidoRM()).single();
    }

    // 3. GENERAR PEDIDO (La joya de la corona: Carrito -> Pedido)
    @Transactional
    public void generarPedido(int usuarioId, int metodoPagoId, BigDecimal importeEnvio) {
        
        // A) Traer items del carrito
        String sqlCarrito = "SELECT productos_id, cantidad, precio FROM detalles_carrito WHERE usuarios_id = ?";
        List<Map<String, Object>> items = jdbcClient.sql(sqlCarrito).param(usuarioId).query().listOfRows();

        if (items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío.");
        }

        // B) Calcular suma de productos
        BigDecimal importeProductos = BigDecimal.ZERO;
        for (Map<String, Object> item : items) {
            BigDecimal precio = (BigDecimal) item.get("precio");
            int cantidad = (Integer) item.get("cantidad");
            importeProductos = importeProductos.add(precio.multiply(new BigDecimal(cantidad)));
        }

        // C) Insertar el PEDIDO (CORREGIDO: Agregamos fecha_hora_pago)
        UUID folio = UUID.randomUUID();
        
        // AQUÍ ESTÁ EL CAMBIO: Agregamos CURRENT_TIMESTAMP
        String sqlInsertPedido = """
            INSERT INTO pedidos (numero, importe_productos, importe_envio, usuarios_id, metodos_pago_id, fecha_hora_pago)
            VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP) 
            RETURNING id
        """;
        
        int pedidoId = jdbcClient.sql(sqlInsertPedido)
                .param(folio)
                .param(importeProductos)
                .param(importeEnvio)
                .param(usuarioId)
                .param(metodoPagoId)
                .query(Integer.class)
                .single();

        // D) Mover items a 'detalles_pedido'
        String sqlDetalle = "INSERT INTO detalles_pedido (cantidad, precio, productos_id, pedidos_id) VALUES (?, ?, ?, ?)";
        for (Map<String, Object> item : items) {
            jdbcClient.sql(sqlDetalle)
                    .param(item.get("cantidad"))
                    .param(item.get("precio"))
                    .param(item.get("productos_id"))
                    .param(pedidoId)
                    .update();
        }

        // E) Vaciar Carrito
        String sqlClean = "DELETE FROM detalles_carrito WHERE usuarios_id = ?";
        jdbcClient.sql(sqlClean).param(usuarioId).update();
    }

    // 5. ACTUALIZAR
public Producto actualizar(int id, Producto p) {
    String sql = """
        UPDATE productos 
        SET nombre=?, precio=?, sku=?, color=?, marca=?, descripcion=?, 
            peso=?, alto=?, ancho=?, profundidad=?, categorias_id=?
        WHERE id = ?
        RETURNING *
    """;
    
    return jdbcClient.sql(sql)
            .param(p.getNombre())
            .param(p.getPrecio())
            .param(p.getSku())
            .param(p.getColor())
            .param(p.getMarca())
            .param(p.getDescripcion())
            .param(p.getPeso())
            .param(p.getAlto())
            .param(p.getAncho())
            .param(p.getProfundidad())
            .param(p.getCategoriaId())
            .param(id)
            .query(new ProductoRM())
            .optional()
            .orElse(null);
}
}