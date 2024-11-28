package com.app.ecomisiones.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // nombre del archivo HTML (login.html)
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("usuario");
        return "login";
    }

    /* @PostMapping("/login")
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
    } */
}
