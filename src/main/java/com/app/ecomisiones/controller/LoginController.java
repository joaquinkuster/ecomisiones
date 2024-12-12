package com.app.ecomisiones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.ecomisiones.model.Carrito;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.service.Carrito.CarritoServiceImpl;
import com.app.ecomisiones.service.Usuario.UsuarioServiceImpl;

import jakarta.validation.Valid;

/**
 * Controlador que maneja las operaciones de inicio de sesión y registro de usuarios.
 * 
 * Este controlador gestiona la vista de inicio de sesión, el registro de nuevos usuarios,
 * y la creación de un carrito asociado a cada usuario registrado.
 */
@Controller
public class LoginController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private CarritoServiceImpl carritoService;

    /**
     * Método que maneja la vista de inicio de sesión.
     * 
     * @param error Mensaje de error si las credenciales son incorrectas.
     * @param logout Mensaje de confirmación si el usuario ha cerrado sesión.
     * @param model El modelo utilizado para pasar datos a la vista.
     * @return El nombre de la vista "login", que muestra el formulario de inicio de sesión.
     */
    @GetMapping("/login")
    public String verLogin(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout, Model model) {
        
        // Si el parámetro 'error' está presente, muestra un mensaje de error
        if (error != null) {
            model.addAttribute("mensaje", "Error! La contraseña o el correo ingresado es inválido.");
        }
        // Si el parámetro 'logout' está presente, muestra un mensaje de cierre de sesión
        else if (logout != null) {
            model.addAttribute("mensaje", "Hecho! Has cerrado sesión correctamente.");
        }
        
        // Establece el título de la vista
        model.addAttribute("titulo", "Iniciar sesión");

        // Retorna la vista 'login'
        return "pages/login";
    }

    /**
     * Método que maneja la vista de registro de un nuevo usuario.
     * 
     * @return El nombre de la vista "registro", que muestra el formulario de registro.
     */
    @GetMapping("/registro")
    public String verRegistro(Model modelo) {
        // Retorna la vista 'registro' para registrar un nuevo usuario
        modelo.addAttribute("titulo", "Registro de cuenta");
        return "pages/registro";
    }

    /**
     * Método que maneja el registro de un nuevo usuario.
     * 
     * @param usuario El objeto de tipo Usuario que contiene los datos del formulario de registro.
     * @param redirectAttributes Atributos utilizados para redirigir con un mensaje de éxito o error.
     * @return La redirección a la página de login con un mensaje de éxito o error.
     */
    @PostMapping("/registro")
    public String regitsrarUsuario(@Valid @ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            // Guarda el nuevo usuario en la base de datos
            Usuario nuevoUsuario = usuarioService.guardar(usuario);

            // Crea un nuevo carrito asociado al usuario recién registrado
            Carrito carrito = new Carrito(nuevoUsuario);
            carritoService.guardar(carrito);

            // Asocia el carrito al usuario
            nuevoUsuario.setCarrito(carrito);

            // Guarda los cambios del usuario (incluyendo la referencia al carrito)
            nuevoUsuario = usuarioService.modificar(nuevoUsuario);

            // Si el usuario no se pudo modificar, lanza una excepción
            if (nuevoUsuario == null) {
                throw new IllegalArgumentException("Error! El usuario ingresado es inválido.");
            }

            // Si todo es correcto, agrega un mensaje de éxito y redirige al login
            redirectAttributes.addFlashAttribute("mensaje", "Hecho! Tu cuenta se ha registrado correctamente.");
            return "redirect:/login";

        } catch (Exception e) {
            // Si ocurre un error, agrega un mensaje de error y redirige al login
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/login";
        }
    }
}
