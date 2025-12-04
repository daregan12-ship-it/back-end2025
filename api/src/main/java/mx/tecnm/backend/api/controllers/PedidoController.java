package mx.tecnm.backend.api.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mx.tecnm.backend.api.models.Pedido;
import mx.tecnm.backend.api.repository.PedidoDAO;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoDAO repo;

    // ID Hardcodeado para pruebas (hasta que tengas Login)
    private final int USUARIO_TEST_ID = 1;

    // GET: Ver historial de mis pedidos
    @GetMapping
    public List<Pedido> misPedidos() {
        return repo.obtenerPedidosPorUsuario(USUARIO_TEST_ID);
    }

    // GET: Ver detalle de un pedido específico
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> verPedido(@PathVariable int id) {
        try {
            Pedido pedido = repo.obtenerPedidoPorId(id);
            return ResponseEntity.ok(pedido);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}