package mx.tecnm.backend.api.controllers;

import mx.tecnm.backend.api.models.DetalleCarrito;
import mx.tecnm.backend.api.repository.CarritoDAO;
import mx.tecnm.backend.api.repository.PedidoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoDAO carritoRepo;
    
    @Autowired
    private PedidoDAO pedidoRepo;

    // YA NO USAMOS EL ID FIJO
    // private final int USUARIO_TEST_ID = 1; <--- BORRADO

    // 1. VER CARRITO
    // URL: GET .../carrito?usuarioId=1
    @GetMapping
    public List<DetalleCarrito> verCarrito(@RequestParam int usuarioId) {
        return carritoRepo.obtenerCarrito(usuarioId);
    }

    // 2. AGREGAR AL CARRITO
    // URL: POST .../carrito?usuarioId=1&productoId=5&cantidad=2
    @PostMapping
    public ResponseEntity<String> agregar(
            @RequestParam int usuarioId, 
            @RequestParam int productoId, 
            @RequestParam int cantidad) {
        
        carritoRepo.agregarProducto(usuarioId, productoId, cantidad);
        return ResponseEntity.ok("Producto agregado al carrito");
    }

    // 3. ELIMINAR UN ITEM
    // URL: DELETE .../carrito/20?usuarioId=1  (Donde 20 es el producto)
    @DeleteMapping("/{productoId}")
    public ResponseEntity<String> eliminarProducto(
            @PathVariable int productoId,
            @RequestParam int usuarioId) { // <--- Lo pedimos como param extra
        
        boolean eliminado = carritoRepo.eliminarProducto(usuarioId, productoId);
        if (eliminado) return ResponseEntity.ok("Producto eliminado del carrito");
        return ResponseEntity.notFound().build();
    }

    // 4. LIMPIAR TODO EL CARRITO
    // URL: DELETE .../carrito?usuarioId=1
    @DeleteMapping
    public ResponseEntity<String> limpiar(@RequestParam int usuarioId) {
        carritoRepo.limpiarCarrito(usuarioId);
        return ResponseEntity.ok("Carrito vaciado");
    }

    // 5. GENERAR PEDIDO (COMPRAR)
    // URL: POST .../carrito/comprar?usuarioId=1&metodoPagoId=2
    @PostMapping("/comprar")
    public ResponseEntity<String> realizarCompra(
            @RequestParam int usuarioId, 
            @RequestParam int metodoPagoId) {
        try {
            BigDecimal envio = new BigDecimal("50.00");
            pedidoRepo.generarPedido(usuarioId, metodoPagoId, envio);
            return ResponseEntity.ok("¡Compra exitosa! Pedido generado y carrito vacío.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}