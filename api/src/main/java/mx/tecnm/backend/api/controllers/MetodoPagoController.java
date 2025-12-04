package mx.tecnm.backend.api.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mx.tecnm.backend.api.models.MetodoPago;
import mx.tecnm.backend.api.repository.MetodoPagoDAO;

@RestController
@RequestMapping("/metodos-pago") // URL base
public class MetodoPagoController {

    @Autowired
    private MetodoPagoDAO repo;

    @GetMapping
    public List<MetodoPago> listar() {
        return repo.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetodoPago> buscar(@PathVariable int id) {
        MetodoPago metodo = repo.buscarPorId(id);
        if (metodo != null) {
            return ResponseEntity.ok(metodo);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    // Ejemplo: .../metodos-pago?nombre=PayPal&comision=3.5
    public ResponseEntity<MetodoPago> crear(@RequestParam String nombre, @RequestParam double comision) {
        MetodoPago creado = repo.crear(nombre, comision);
        return ResponseEntity.ok(creado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        // Soft Delete: Desactivar en vez de borrar
        boolean desactivado = repo.eliminar(id);
        if (desactivado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetodoPago> actualizar(
            @PathVariable int id, 
            @RequestParam String nombre, 
            @RequestParam double comision) {
        
        MetodoPago actualizado = repo.actualizar(id, nombre, comision);
        
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}