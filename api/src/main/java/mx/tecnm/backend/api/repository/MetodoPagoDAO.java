package mx.tecnm.backend.api.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import mx.tecnm.backend.api.models.MetodoPago;

@Repository
public class MetodoPagoDAO {

    @Autowired
    private JdbcClient jdbcClient;

    // 1. LISTAR (Solo activos)
    public List<MetodoPago> listar() {
        String sql = "SELECT * FROM metodos_pago WHERE activo = true";
        return jdbcClient.sql(sql).query(new MetodoPagoRM()).list();
    }

    // 2. BUSCAR POR ID
    public MetodoPago buscarPorId(int id) {
        String sql = "SELECT * FROM metodos_pago WHERE id = ? AND activo = true";
        return jdbcClient.sql(sql)
                .param(id)
                .query(new MetodoPagoRM())
                .optional()
                .orElse(null);
    }

    // 3. CREAR INTELIGENTE (Inserta o Reactiva por Nombre)
    public MetodoPago crear(String nombre, double comision) {
        // A) Buscar por nombre (incluso inactivos)
        String sqlBuscar = "SELECT * FROM metodos_pago WHERE nombre = ?";
        var existente = jdbcClient.sql(sqlBuscar).param(nombre).query(new MetodoPagoRM()).optional();

        if (existente.isPresent()) {
            MetodoPago mp = existente.get();
            if (!mp.isActivo()) {
                // B) RESUCITAR
                return actualizar(mp.getId(), nombre, comision); // Reusamos actualizar
            } else {
                return mp; // Ya existe activo, lo devolvemos
            }
        }

        // C) INSERTAR NUEVO
        String sql = "INSERT INTO metodos_pago (nombre, comision, activo) VALUES (?, ?, true) RETURNING *";
        return jdbcClient.sql(sql)
                .param(nombre)
                .param(comision)
                .query(new MetodoPagoRM())
                .single();
    }

    // 4. ACTUALIZAR (Y Resucitar)
    public MetodoPago actualizar(int id, String nombre, double comision) {
        // Forzamos activo = true en el update
        String sql = "UPDATE metodos_pago SET nombre = ?, comision = ?, activo = true WHERE id = ? RETURNING *";
        return jdbcClient.sql(sql)
                .param(nombre)
                .param(comision)
                .param(id)
                .query(new MetodoPagoRM())
                .optional()
                .orElse(null);
    }

    // 5. ELIMINAR (Soft Delete)
    public boolean eliminar(int id) {
        String sql = "UPDATE metodos_pago SET activo = false WHERE id = ?";
        int filas = jdbcClient.sql(sql).param(id).update();
        return filas > 0;
    }
}