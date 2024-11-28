package com.app.ecomisiones.service.Usuario;

import java.util.Optional;

import com.app.ecomisiones.model.Usuario;

public interface UsuarioService {

    Optional<Usuario> iniciarSesion(String correo, String password);

}
