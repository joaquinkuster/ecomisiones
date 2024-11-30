package com.app.ecomisiones.service.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.repository.UsuarioRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService, CrudService<Usuario> {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario guardar(Usuario usuario) {
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new RuntimeException("El usuario con este email ya está registrado");
        }
        // Cifrar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .filter(usuario -> !usuario.esInactivo()); // Solo devuelve usuarios activos
    }

    @Override
    public List<Usuario> obtenerTodo() {
        return usuarioRepository.findAll(); // Solo devuelve usuarios activos
    }

    @Override
    public Usuario modificar(Usuario usuario) {
        // Otros campos actualizables
        return usuarioRepository.save(usuario);
    }

    @Override
    public void borrar(Usuario usuario) {
        usuarioRepository.delete(usuario);
    }

    @Override
    public boolean existePorId(Integer id) {
        return usuarioRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }
}
