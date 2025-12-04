package mx.tecnm.backend.api.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import mx.tecnm.backend.api.models.Pedido;

public class PedidoRM implements RowMapper<Pedido> {

    @Override
    public Pedido mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Pedido(
            rs.getInt("id"),
            rs.getTimestamp("fecha"),
            rs.getString("numero"), // UUID viene como String
            rs.getBigDecimal("importe_productos"),
            rs.getBigDecimal("importe_envio"),
            rs.getInt("usuarios_id"),
            rs.getInt("metodos_pago_id"),
            rs.getTimestamp("fecha_hora_pago"),
            rs.getBigDecimal("importe_iva"),
            rs.getBigDecimal("total")
        );
    }
}