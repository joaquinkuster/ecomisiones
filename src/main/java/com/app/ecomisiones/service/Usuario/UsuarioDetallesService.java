package com.app.ecomisiones.service.Usuario;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.repository.UsuarioRepository;

/**
 * Servicio que implementa la interfaz {@link UserDetailsService} para cargar los detalles del usuario
 * basándose en el nombre de usuario (correo electrónico). Este servicio es utilizado por Spring Security
 * para autenticar al usuario y cargar su información.
 */
@Service
public class UsuarioDetallesService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor de la clase {@link UsuarioDetallesService}.
     * 
     * @param usuarioRepository El repositorio que se utilizará para acceder a los datos de usuario.
     */
    public UsuarioDetallesService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Carga el usuario basándose en el nombre de usuario (correo electrónico).
     * 
     * @param username El nombre de usuario (correo electrónico) que se desea autenticar.
     * @return Los detalles del usuario cargado.
     * @throws UsernameNotFoundException Si el usuario no es encontrado o está inactivo.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar el usuario por su correo y asegurarse de que esté activo
        Optional<Usuario> usuario = usuarioRepository.findByCorreoAndBajaFalse(username);
        
        // Si el usuario no se encuentra o está inactivo, lanzar una excepción
        if (!usuario.isPresent()) {
            throw new UsernameNotFoundException("Usuario no encontrado o inactivo");
        }
        
        // Devolver los detalles del usuario encontrado
        return usuario.get();
    }
}
