package com.app.ecomisiones.service.Carrito;

import com.app.ecomisiones.model.Carrito;
import com.app.ecomisiones.model.Usuario;

/**
 * Servicio para gestionar las operaciones relacionadas con los carritos de los usuarios.
 */
public interface CarritoService {

    /**
     * Busca el carrito asociado a un usuario.
     * 
     * @param usuario El usuario cuyo carrito se desea buscar.
     * @return El carrito asociado al usuario.
     */
    Carrito buscarPorUsuario(Usuario usuario);
}
