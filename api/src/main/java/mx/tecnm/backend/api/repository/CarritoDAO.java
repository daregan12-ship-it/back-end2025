package mx.tecnm.backend.api.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import mx.tecnm.backend.api.models.DetalleCarrito;

@Repository
public class CarritoDAO {

    @Autowired
    private JdbcClient jdbcClient;

    // 1. VER CARRITO (De un usuario)
    // Usamos DetalleCarritoRM que creamos en el otro archivo
    public List<DetalleCarrito> obtenerCarrito(int usuarioId) {
        String sql = """
            SELECT dc.id, dc.cantidad, dc.precio, dc.productos_id, dc.usuarios_id, p.nombre as nombre_prod
            FROM detalles_carrito dc
            JOIN productos p ON dc.productos_id = p.id
            WHERE dc.usuarios_id = ?
        """;
        return jdbcClient.sql(sql)
                .param(usuarioId)
                .query(new DetalleCarritoRM()) // Llamamos al mapper externo
                .list();
    }

    // 2. AGREGAR PRODUCTO (Insertar o Incrementar)
    public void agregarProducto(int usuarioId, int productoId, int cantidad) {
        // A) ¿Ya existe este producto en el carrito de este usuario?
        String sqlCheck = "SELECT count(*) FROM detalles_carrito WHERE usuarios_id = ? AND productos_id = ?";
        Integer count = jdbcClient.sql(sqlCheck).param(usuarioId).param(productoId).query(Integer.class).single();

        if (count > 0) {
            // B) SI EXISTE -> Sumamos cantidad
            String sqlUpdate = "UPDATE detalles_carrito SET cantidad = cantidad + ? WHERE usuarios_id = ? AND productos_id = ?";
            jdbcClient.sql(sqlUpdate).param(cantidad).param(usuarioId).param(productoId).update();
        } else {
            // C) NO EXISTE -> Buscamos precio actual e insertamos
            String sqlPrecio = "SELECT precio FROM productos WHERE id = ?";
            var precio = jdbcClient.sql(sqlPrecio).param(productoId).query(java.math.BigDecimal.class).single();

            String sqlInsert = "INSERT INTO detalles_carrito (usuarios_id, productos_id, cantidad, precio) VALUES (?, ?, ?, ?)";
            jdbcClient.sql(sqlInsert).param(usuarioId).param(productoId).param(cantidad).param(precio).update();
        }
    }

    // 3. ELIMINAR UN ITEM (Borrado real)
// 3. ELIMINAR UN ITEM (Lógica Inteligente: Disminuir o Borrar)
public boolean eliminarProducto(int usuarioId, int productoId) {
    // A) Primero verificamos cuántos hay actualmente
    String sqlCheck = "SELECT cantidad FROM detalles_carrito WHERE usuarios_id = ? AND productos_id = ?";
    
    // Usamos .optional() por si el producto no existe, que no truene
    var resultado = jdbcClient.sql(sqlCheck)
            .param(usuarioId)
            .param(productoId)
            .query(Integer.class)
            .optional();

    if (resultado.isEmpty()) {
        return false; // No existía el producto, no se hizo nada
    }

    int cantidadActual = resultado.get();

    if (cantidadActual > 1) {
        // B) Si hay más de 1, SOLO DISMINUIMOS la cantidad (UPDATE)
        String sqlUpdate = "UPDATE detalles_carrito SET cantidad = cantidad - 1 WHERE usuarios_id = ? AND productos_id = ?";
        jdbcClient.sql(sqlUpdate).param(usuarioId).param(productoId).update();
        return true; // Se modificó con éxito
    } else {
        // C) Si queda 1 (o menos), HACEMOS EL BORRADO REAL (DELETE)
        String sqlDelete = "DELETE FROM detalles_carrito WHERE usuarios_id = ? AND productos_id = ?";
        jdbcClient.sql(sqlDelete).param(usuarioId).param(productoId).update();
        return true; // Se eliminó con éxito
    }
}

    // 4. LIMPIAR CARRITO COMPLETO
    public void limpiarCarrito(int usuarioId) {
        String sql = "DELETE FROM detalles_carrito WHERE usuarios_id = ?";
        jdbcClient.sql(sql).param(usuarioId).update();
    }
}