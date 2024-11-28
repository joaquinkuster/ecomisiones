package com.app.ecomisiones.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.service.Usuario.UsuarioServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    @Lazy
    private UsuarioServiceImpl usuarioService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // nombre del archivo HTML (login.html)
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("correo") String correo, 
            @RequestParam("password") String password, 
            Model model, HttpSession session) {
        
        
         // Verificamos si existe un estudiante con el DNI y la contraseña
        Optional<Usuario> estudianteOpt = usuarioService.iniciarSesion(correo, password);

        if (estudianteOpt.isPresent()) {
            // Si el estudiante existe, generamos un token de sesión
            String token = UUID.randomUUID().toString();
            session.setAttribute("token", token);

            // Agregamos el token al modelo para que pueda ser utilizado en la vista
            model.addAttribute("token", token);
            model.addAttribute("nombre", estudianteOpt.toString());

            return "index"; // Página a la que se redirige después de iniciar sesión
        } else {
            model.addAttribute("error", "Correo o contraseña incorrectos.");
            return "login";
        }
    }
}
