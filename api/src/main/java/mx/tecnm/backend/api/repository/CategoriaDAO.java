package mx.tecnm.backend.api.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.Categoria;

@Repository
public class CategoriaDAO{
    @Autowired
    private JdbcClient jdbcClient;

    public List<Categoria> obtenerCategorias() {
        String sql = "SELECT id, nombre FROM categorias";
        return jdbcClient.sql(sql).query(new CategoriaRM()).list();
    }


    public Categoria obtenerCategoriasPorId (int id){
        String sql = "SELECT id, nombre FROM categorias WHERE id = ?";
        return jdbcClient.sql(sql).param(id).query(new CategoriaRM()).single();
    }


    public Categoria crearCategoria (String nuevaCategoria){
        String sql = "Insert into categorias (nombre) values (?) returning id, nombre";
        return jdbcClient.sql(sql).param(nuevaCategoria).query(new CategoriaRM()).optional()
            .orElse(null);
    }


    public Categoria actualizarCategoria(int id, String nombreNuevo) {
    String sql = """
        UPDATE categorias
        SET nombre = ?
        WHERE id = ?
        RETURNING id, nombre
    """;
    return jdbcClient.sql(sql).param(nombreNuevo).param(id).query(new CategoriaRM()).optional().orElse(null);
}

}