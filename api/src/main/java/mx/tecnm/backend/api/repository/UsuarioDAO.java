package mx.tecnm.backend.api.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import mx.tecnm.backend.api.models.Usuario;

@Repository
public class UsuarioDAO {

    @Autowired
    private JdbcClient jdbcClient;

    // 1. LISTAR (Solo mostramos los usuarios activos)
    public List<Usuario> listar() {
        String sql = "SELECT * FROM usuarios WHERE activo = true";
        return jdbcClient.sql(sql).query(new UsuarioRM()).list();
    }

    // 2. BUSCAR POR ID (Solo si está activo)
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ? AND activo = true";
        return jdbcClient.sql(sql)
                .param(id)
                .query(new UsuarioRM())
                .optional()
                .orElse(null);
    }

    // 3. CREAR INTELIGENTE (Inserta nuevo o Reactiva si ya existía)
    public Usuario crear(Usuario nuevoUsuario) {
        // Paso A: Buscamos si el correo ya existe (incluso si está inactivo)
        String sqlBuscar = "SELECT * FROM usuarios WHERE email = ?";
        
        var existente = jdbcClient.sql(sqlBuscar)
                .param(nuevoUsuario.getEmail())
                .query(new UsuarioRM())
                .optional();

        if (existente.isPresent()) {
            Usuario u = existente.get();
            // Paso B: Si existe pero está inactivo -> ¡LO RESUCITAMOS!
            if (!u.isActivo()) {
                String sqlReactivar = """
                    UPDATE usuarios 
                    SET nombre = ?, telefono = ?, sexo = ?::sexo_enum, 
                        fecha_nacimiento = ?, contrasena = ?, activo = true 
                    WHERE id = ? 
                    RETURNING *
                """;
                return jdbcClient.sql(sqlReactivar)
                        .param(nuevoUsuario.getNombre())
                        .param(nuevoUsuario.getTelefono())
                        .param(nuevoUsuario.getSexo())
                        .param(nuevoUsuario.getFechaNacimiento())
                        .param(nuevoUsuario.getContrasena())
                        .param(u.getId()) // Usamos el ID viejo recuperado
                        .query(new UsuarioRM())
                        .single();
            } else {
                // Paso C: Si ya existe y está activo -> Error (No duplicados)
                throw new RuntimeException("El correo electrónico ya está registrado.");
            }
        } 
        
        // Paso D: Si NO existe -> INSERTAMOS UNO NUEVO
        // Nota: ?::sexo_enum es necesario para que Postgres entienda el String como ENUM
        String sqlInsertar = """
            INSERT INTO usuarios (nombre, email, telefono, sexo, fecha_nacimiento, contrasena, activo) 
            VALUES (?, ?, ?, ?::sexo_enum, ?, ?, true) 
            RETURNING *
        """;
        
        return jdbcClient.sql(sqlInsertar)
                .param(nuevoUsuario.getNombre())
                .param(nuevoUsuario.getEmail())
                .param(nuevoUsuario.getTelefono())
                .param(nuevoUsuario.getSexo())
                .param(nuevoUsuario.getFechaNacimiento())
                .param(nuevoUsuario.getContrasena())
                .query(new UsuarioRM())
                .single();
    }

    // 4. ELIMINAR (Soft Delete / Desactivar)
    public boolean eliminar(int id) {
        String sql = "UPDATE usuarios SET activo = false WHERE id = ?";
        int filas = jdbcClient.sql(sql).param(id).update();
        return filas > 0;
    }

    // 5. ACTUALIZAR (Modificar datos de un usuario existente)
    public Usuario actualizar(int id, Usuario u) {
        String sql = """
            UPDATE usuarios 
            SET nombre = ?, email = ?, telefono = ?, sexo = ?::sexo_enum, fecha_nacimiento = ?
            WHERE id = ? 
            RETURNING *
        """;
        // Nota: No actualizamos contraseña aquí por seguridad
        return jdbcClient.sql(sql)
                .param(u.getNombre())
                .param(u.getEmail())
                .param(u.getTelefono())
                .param(u.getSexo())
                .param(u.getFechaNacimiento())
                .param(id)
                .query(new UsuarioRM())
                .optional()
                .orElse(null);
    }
}