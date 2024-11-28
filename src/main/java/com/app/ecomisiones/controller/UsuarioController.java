package com.app.ecomisiones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.service.Usuario.UsuarioServiceImpl;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @GetMapping("/registro")
    public String registro(HttpSession session) {
        return "registro";
    }

    @PostMapping("/registro")
    public String registro(@Valid @ModelAttribute Usuario usuario) {
        try {

            Usuario nuevoUsuario = usuarioService.guardar(usuario);
            if (nuevoUsuario == null) {
                return "registro";
            }

            return "login";
        } catch (Exception e) {
            return "registro";

        }
    }
}
