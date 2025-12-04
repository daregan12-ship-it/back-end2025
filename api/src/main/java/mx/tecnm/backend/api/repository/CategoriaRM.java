package mx.tecnm.backend.api.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import mx.tecnm.backend.api.models.Categoria;

public class CategoriaRM implements RowMapper<Categoria> {

    @Override
    public Categoria mapRow(ResultSet rs, int rowNum) throws SQLException {
        // CORRECCIÓN: Ahora le pasamos los 3 datos que pide el constructor
        return new Categoria(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getBoolean("activo") // <-- Agregamos esto
        );
    }
}