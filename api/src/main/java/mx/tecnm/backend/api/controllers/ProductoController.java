package mx.tecnm.backend.api.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mx.tecnm.backend.api.models.Producto;
import mx.tecnm.backend.api.repository.ProductoDAO;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoDAO repo;

    @GetMapping
    public List<Producto> listar() {
        return repo.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscar(@PathVariable int id) {
        Producto prod = repo.buscarPorId(id);
        if (prod != null) {
            return ResponseEntity.ok(prod);
        }
        return ResponseEntity.notFound().build();
    }

    // POST usando JSON
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto nuevoProducto) {
        // Validación básica (opcional)
        if (nuevoProducto.getCategoriaId() <= 0) {
             return ResponseEntity.badRequest().build();
        }
        
        Producto creado = repo.crear(nuevoProducto);
        return ResponseEntity.ok(creado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        boolean desactivado = repo.eliminar(id);
        if (desactivado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable int id, @RequestBody Producto productoDatos) {
        Producto actualizado = repo.actualizar(id, productoDatos);
        
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}