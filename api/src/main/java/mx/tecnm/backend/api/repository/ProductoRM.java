package mx.tecnm.backend.api.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import mx.tecnm.backend.api.models.Producto;

public class ProductoRM implements RowMapper<Producto> {
    @Override
    public Producto mapRow(ResultSet rs, int rowNum) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setSku(rs.getString("sku"));
        p.setColor(rs.getString("color"));
        p.setMarca(rs.getString("marca"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPeso(rs.getBigDecimal("peso"));
        p.setAlto(rs.getBigDecimal("alto"));
        p.setAncho(rs.getBigDecimal("ancho"));
        p.setProfundidad(rs.getBigDecimal("profundidad"));
        p.setCategoriaId(rs.getInt("categorias_id")); // Ojo con la 's' del nombre en BD
        p.setActivo(rs.getBoolean("activo"));
        return p;
    }
}