package mx.tecnm.backend.api.repository;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import mx.tecnm.backend.api.models.Categoria;

public class CategoriaRM implements RowMapper<Categoria> {

    @Override
    public Categoria mapRow(java.sql.ResultSet rs, int rowNum) throws SQLException {
        return new Categoria(
            rs.getInt("id"),
            rs.getString("nombre") );
    }
}