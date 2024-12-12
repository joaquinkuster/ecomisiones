package com.app.ecomisiones.service.Usuario;

import java.util.Optional;

import com.app.ecomisiones.model.Usuario;

/**
 * Servicio para gestionar las operaciones relacionadas con los usuarios.
 * Proporciona métodos para realizar consultas y manipulaciones relacionadas con los usuarios.
 */
public interface UsuarioService {

    /**
     * Busca un usuario en el sistema a partir de su correo electrónico.
     * 
     * @param correo el correo electrónico del usuario a buscar
     * @return un {@link Optional} que contiene al usuario si se encuentra, o vacío si no se encuentra
     */
    Optional<Usuario> buscarPorCorreo(String correo);

}
