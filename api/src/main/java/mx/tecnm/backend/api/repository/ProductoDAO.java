package mx.tecnm.backend.api.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import mx.tecnm.backend.api.models.Producto;

@Repository
public class ProductoDAO {

    @Autowired
    private JdbcClient jdbcClient;

    // 1. LISTAR (Solo activos)
    public List<Producto> listar() {
        String sql = "SELECT * FROM productos WHERE activo = true";
        return jdbcClient.sql(sql).query(new ProductoRM()).list();
    }

    // 2. BUSCAR POR ID
    public Producto buscarPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id = ? AND activo = true";
        return jdbcClient.sql(sql)
                .param(id)
                .query(new ProductoRM())
                .optional()
                .orElse(null);
    }

    // 3. CREAR INTELIGENTE (Inserta o Reactiva por SKU)
    public Producto crear(Producto p) {
        // A) Buscamos si existe el SKU (incluso borrados)
        String sqlBuscar = "SELECT * FROM productos WHERE sku = ?";
        var existente = jdbcClient.sql(sqlBuscar).param(p.getSku()).query(new ProductoRM()).optional();

        if (existente.isPresent()) {
            Producto viejo = existente.get();
            if (!viejo.isActivo()) {
                // B) RESUCITAR: Actualizamos datos y ponemos activo = true
                return actualizar(viejo.getId(), p); // Reusamos el update pero forzando activo
            } else {
                throw new RuntimeException("El SKU " + p.getSku() + " ya existe y está activo.");
            }
        }

        // C) INSERTAR NUEVO
        String sql = """
            INSERT INTO productos 
            (nombre, precio, sku, color, marca, descripcion, peso, alto, ancho, profundidad, categorias_id, activo)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true)
            RETURNING *
        """;
        
        return jdbcClient.sql(sql)
                .param(p.getNombre()).param(p.getPrecio()).param(p.getSku())
                .param(p.getColor()).param(p.getMarca()).param(p.getDescripcion())
                .param(p.getPeso()).param(p.getAlto()).param(p.getAncho()).param(p.getProfundidad())
                .param(p.getCategoriaId())
                .query(new ProductoRM())
                .single();
    }

    // 4. ACTUALIZAR (También sirve para Resucitar)
    public Producto actualizar(int id, Producto p) {
        // En el UPDATE forzamos activo = true por si estamos resucitando
        String sql = """
            UPDATE productos 
            SET nombre=?, precio=?, sku=?, color=?, marca=?, descripcion=?, 
                peso=?, alto=?, ancho=?, profundidad=?, categorias_id=?, activo=true
            WHERE id = ?
            RETURNING *
        """;
        
        return jdbcClient.sql(sql)
                .param(p.getNombre()).param(p.getPrecio()).param(p.getSku())
                .param(p.getColor()).param(p.getMarca()).param(p.getDescripcion())
                .param(p.getPeso()).param(p.getAlto()).param(p.getAncho()).param(p.getProfundidad())
                .param(p.getCategoriaId())
                .param(id)
                .query(new ProductoRM())
                .optional()
                .orElse(null);
    }

    // 5. ELIMINAR (Soft Delete)
    public boolean eliminar(int id) {
        String sql = "UPDATE productos SET activo = false WHERE id = ?";
        int filas = jdbcClient.sql(sql).param(id).update();
        return filas > 0;
    }
}