package mx.tecnm.backend.api.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import mx.tecnm.backend.api.models.Usuario;

public class UsuarioRM implements RowMapper<Usuario> {

    @Override
    public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
        Usuario user = new Usuario();
        
        // Mapeamos columna por columna de la BD al objeto Java
        user.setId(rs.getInt("id"));
        user.setNombre(rs.getString("nombre"));
        user.setEmail(rs.getString("email"));
        user.setTelefono(rs.getString("telefono"));
        user.setSexo(rs.getString("sexo")); // Postgres devuelve el ENUM 'H'/'M' como String
        user.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
        user.setContrasena(rs.getString("contrasena"));
        user.setActivo(rs.getBoolean("activo")); // Campo clave para el "Soft Delete"

        return user;
    }
}