package com.app.ecomisiones.service.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.repository.UsuarioRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestionar las operaciones sobre la entidad {@link Usuario}.
 * Esta clase proporciona las operaciones CRUD básicas (Crear, Leer, Actualizar, Eliminar)
 * para la entidad Usuario, además de funcionalidades adicionales como la verificación de
 * usuarios por correo y la gestión de contraseñas cifradas.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService, CrudService<Usuario> {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Guarda un nuevo usuario en la base de datos. Antes de guardarlo, se valida que
     * no exista otro usuario con el mismo correo electrónico. Además, la contraseña es
     * cifrada antes de ser almacenada.
     * 
     * @param usuario El usuario a guardar.
     * @return El usuario guardado.
     * @throws RuntimeException Si ya existe un usuario con el mismo correo.
     */
    @Override
    public Usuario guardar(Usuario usuario) {
        // Verificar si el correo ya está registrado
        if (usuarioRepository.findByCorreoAndBajaFalse(usuario.getCorreo()).isPresent()) {
            throw new RuntimeException("El usuario con este email ya está registrado");
        }
        // Cifrar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    /**
     * Busca un usuario por su identificador. Solo devuelve usuarios activos.
     * 
     * @param id El identificador del usuario.
     * @return Un {@link Optional} con el usuario encontrado si está activo.
     */
    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .filter(usuario -> !usuario.esInactivo()); // Solo devuelve usuarios activos
    }

    /**
     * Obtiene todos los usuarios activos.
     * 
     * @return Una lista de todos los usuarios activos.
     */
    @Override
    public List<Usuario> obtenerTodo() {
        return usuarioRepository.findAll(); // Devuelve todos los usuarios
    }

    /**
     * Modifica un usuario existente.
     * 
     * @param usuario El usuario con los datos actualizados.
     * @return El usuario modificado.
     */
    @Override
    public Usuario modificar(Usuario usuario) {
        return usuarioRepository.save(usuario); // Guarda el usuario modificado
    }

    /**
     * Elimina un usuario de la base de datos.
     * 
     * @param usuario El usuario a eliminar.
     */
    @Override
    public void borrar(Usuario usuario) {
        usuarioRepository.delete(usuario); // Elimina el usuario
    }

    /**
     * Verifica si un usuario con el identificador dado existe y está activo.
     * 
     * @param id El identificador del usuario.
     * @return true si el usuario existe y está activo, false en caso contrario.
     */
    @Override
    public boolean existePorId(Integer id) {
        return usuarioRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }

    /**
     * Busca un usuario por su correo electrónico. Solo devuelve usuarios activos.
     * 
     * @param correo El correo electrónico del usuario.
     * @return Un {@link Optional} con el usuario encontrado si está activo.
     */
    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreoAndBajaFalse(correo);
    }
}
