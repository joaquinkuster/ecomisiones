package com.app.ecomisiones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.service.Usuario.UsuarioServiceImpl;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios/")
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.obtenerTodo();
    }

    @GetMapping("{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        return usuarioService.guardar(usuario);
    }

    @PutMapping("{id}")
    public ResponseEntity<Usuario> modificar(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            return ResponseEntity.ok(usuarioService.modificar(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> borrar(@PathVariable Usuario usuario) {
        usuarioService.borrar(usuario);
        return ResponseEntity.noContent().build();
    }
}
