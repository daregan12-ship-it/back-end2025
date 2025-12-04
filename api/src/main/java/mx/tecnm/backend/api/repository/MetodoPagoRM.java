package mx.tecnm.backend.api.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import mx.tecnm.backend.api.models.MetodoPago;

public class MetodoPagoRM implements RowMapper<MetodoPago> {

    @Override
    public MetodoPago mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Usamos el constructor que recibe los 4 datos:
        // 1. ID
        // 2. Nombre
        // 3. Comisión
        // 4. Activo (Para el Soft Delete)
        return new MetodoPago(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getDouble("comision"),
            rs.getBoolean("activo") 
        );
    }
}