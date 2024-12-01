package com.app.ecomisiones.service.Carrito;

import com.app.ecomisiones.model.Carrito;
import com.app.ecomisiones.model.Usuario;

public interface CarritoService {
    Carrito buscarPorUsuario(Usuario usuario);

}
