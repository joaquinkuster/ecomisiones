package com.app.ecomisiones.service.Usuario;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.repository.UsuarioRepository;

@Service
public class UsuarioDetallesService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetallesService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(username);
        System.out.println("nombre:   " + username);
        if (!usuario.isPresent()) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        } 
        return usuario.get();
    }
}