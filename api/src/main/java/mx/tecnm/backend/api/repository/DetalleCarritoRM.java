package mx.tecnm.backend.api.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import mx.tecnm.backend.api.models.DetalleCarrito;

public class DetalleCarritoRM implements RowMapper<DetalleCarrito> {

    @Override
    public DetalleCarrito mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Mapeamos los datos que vienen del JOIN entre 'detalles_carrito' y 'productos'
        return new DetalleCarrito(
            rs.getInt("id"),
            rs.getInt("cantidad"),
            rs.getBigDecimal("precio"),
            rs.getInt("productos_id"),
            rs.getString("nombre_prod"), // Este es el alias que pusimos en el SQL del DAO
            rs.getInt("usuarios_id")
        );
    }
}