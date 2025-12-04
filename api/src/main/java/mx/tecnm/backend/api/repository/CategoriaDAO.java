package mx.tecnm.backend.api.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional; // Importante para la cascada
import mx.tecnm.backend.api.models.Categoria;

@Repository
public class CategoriaDAO {

    @Autowired
    private JdbcClient jdbcClient;

    // 1. OBTENER TODAS (Solo activas)
    public List<Categoria> obtenerCategorias() {
        String sql = "SELECT * FROM categorias WHERE activo = true";
        return jdbcClient.sql(sql).query(new CategoriaRM()).list();
    }

    // 2. OBTENER POR ID
    public Categoria obtenerCategoriasPorId(int id) {
        String sql = "SELECT * FROM categorias WHERE id = ? AND activo = true";
        return jdbcClient.sql(sql)
                        .param(id)
                        .query(new CategoriaRM())
                        .single();
    }

    // 3. CREAR INTELIGENTE (Inserta o Reactiva)
    public Categoria crearCategoria(String nuevaCategoria) {
        // Paso A: Buscamos si ya existe el nombre (incluso si está inactivo)
        String sqlBuscar = "SELECT * FROM categorias WHERE nombre = ?";
        
        Optional<Categoria> existente = jdbcClient.sql(sqlBuscar)
                .param(nuevaCategoria)
                .query(new CategoriaRM())
                .optional();

        if (existente.isPresent()) {
            // Paso B: Si existe...
            Categoria cat = existente.get();
            if (!cat.isActivo()) {
                // ...y está desactivada -> LA REACTIVAMOS (UPDATE)
                String sqlReactivar = "UPDATE categorias SET activo = true WHERE id = ? RETURNING *";
                return jdbcClient.sql(sqlReactivar)
                        .param(cat.getId())
                        .query(new CategoriaRM())
                        .single();
            }
            // Si existe y ya está activa, simplemente la devolvemos (no creamos duplicado)
            return cat; 
        } else {
            // Paso C: Si NO existe -> INSERTAMOS normal
            String sqlInsertar = "INSERT INTO categorias (nombre) VALUES (?) RETURNING *";
            return jdbcClient.sql(sqlInsertar)
                    .param(nuevaCategoria)
                    .query(new CategoriaRM())
                    .single();
        }
    }

    // 4. ACTUALIZAR
    public Categoria actualizarCategoria(int id, String nombreNuevo) {
        String sql = "UPDATE categorias SET nombre = ? WHERE id = ? RETURNING *";
        return jdbcClient.sql(sql)
                .param(nombreNuevo)
                .param(id)
                .query(new CategoriaRM())
                .optional()
                .orElse(null);
    }

    // 5. ELIMINAR EN CASCADA (Desactiva Categoría Y sus Productos)
    @Transactional // Esto asegura que se hagan los dos UPDATEs o ninguno
    public boolean eliminarCategoria(int id) {
        // A) Desactivar la Categoría
        String sqlCat = "UPDATE categorias SET activo = false WHERE id = ?";
        int filas = jdbcClient.sql(sqlCat).param(id).update();

        // B) Si se desactivó la categoría, desactivar también sus productos (Cascada)
        if (filas > 0) {
            String sqlProd = "UPDATE productos SET activo = false WHERE categorias_id = ?";
            jdbcClient.sql(sqlProd).param(id).update();
        }
        
        return filas > 0;
    }
}