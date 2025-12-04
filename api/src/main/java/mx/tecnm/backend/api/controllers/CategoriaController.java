package mx.tecnm.backend.api.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Importamos todo para incluir DeleteMapping
import mx.tecnm.backend.api.models.Categoria;
import mx.tecnm.backend.api.repository.CategoriaDAO;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaDAO repo;

    @GetMapping()
    public ResponseEntity<List<Categoria>> obtenerCategorias() {
        List<Categoria> resultado = repo.obtenerCategorias();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoriasPorId(@PathVariable int id) {
        System.out.println("ID recibido: " + id);
        Categoria categoria = repo.obtenerCategoriasPorId(id);
        if (categoria != null) {
            return ResponseEntity.ok(categoria);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<Categoria> crearCategoria(@RequestParam String nuevaCategoria) {
        Categoria categoriaCreada = repo.crearCategoria(nuevaCategoria);
        return ResponseEntity.ok(categoriaCreada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable int id, @RequestParam String nombreNuevo) {
        Categoria categoriaActualizada = repo.actualizarCategoria(id, nombreNuevo);
        if (categoriaActualizada == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(categoriaActualizada);
        }
    }

    // --- AQUÍ ESTÁ LO NUEVO PARA EL "BORRADO LÓGICO" ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable int id) {
        // Llamamos al DAO que ya configuramos para hacer UPDATE activo = false
        boolean seDesactivo = repo.eliminarCategoria(id);

        if (seDesactivo) {
            return ResponseEntity.ok().build(); // 200 OK (Éxito)
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found (No existía el ID)
        }
    }
}