package mx.tecnm.backend.api.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mx.tecnm.backend.api.models.Usuario;
import mx.tecnm.backend.api.repository.UsuarioDAO;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioDAO repo;

    @GetMapping
    public List<Usuario> listar() {
        return repo.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable int id) {
        Usuario usuario = repo.buscarPorId(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

    // OJO AQUÍ: Usamos @RequestBody para recibir un JSON completo
    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario nuevoUsuario) {
        Usuario creado = repo.crear(nuevoUsuario);
        return ResponseEntity.ok(creado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        // Soft Delete
        boolean desactivado = repo.eliminar(id);
        if (desactivado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable int id, @RequestBody Usuario usuarioDatos) {
        Usuario actualizado = repo.actualizar(id, usuarioDatos);
        
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}