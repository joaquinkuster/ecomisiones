package com.app.ecomisiones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.ecomisiones.model.Carrito;
import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.service.Carrito.CarritoServiceImpl;
import com.app.ecomisiones.service.Categoria.CategoriaServiceImpl;
import com.app.ecomisiones.service.Usuario.UsuarioServiceImpl;

import jakarta.validation.Valid;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private CategoriaServiceImpl categoriaService;

    @Autowired
    private  CarritoServiceImpl carritoService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Registro de una Nueva Cuenta
    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registro(@Valid @ModelAttribute Usuario usuario) {
        try {

            Usuario nuevoUsuario = usuarioService.guardar(usuario);

            Carrito carrito = new Carrito(nuevoUsuario);
            carritoService.guardar(carrito);

            nuevoUsuario.setCarrito(carrito);
            nuevoUsuario = usuarioService.modificar(nuevoUsuario);

            if (nuevoUsuario == null) {
                return "registro";
            }

            return "login";
        } catch (Exception e) {
            return "registro";

        }
    }

    // Ver Perfil
    @GetMapping("/usuarios/verPerfil")
    public String verPerfil(Model modelo,
            Authentication auth) {

        List<Categoria> categorias = categoriaService.obtenerTodo();

        modelo.addAttribute("categorias", categorias);
        modelo.addAttribute("usuario", (Usuario) auth.getPrincipal());

        return "perfil/verPerfil";
    }

    // Modificar un Perfil
    @PostMapping("/usuarios/modificar/{id}")
    public String modificarPerfil(@PathVariable int id,
            @RequestParam(name = "nombre") String nombre,
            @RequestParam(name = "apellido") String apellido,
            @RequestParam(name = "correo") String correo,
            @RequestParam(name = "telefono", required = false) String telefono) {
        try {

            Usuario usuario = usuarioService.buscarPorId(id).orElse(null);

            if (usuario == null) {
                throw new IllegalArgumentException("Error! El usuario ingresado no existe.");
            }

            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setCorreo(correo);
            usuario.setTelefono(telefono);

            usuario = usuarioService.modificar(usuario);

            SecurityContextHolder.getContext()
                    .setAuthentication(
                            new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()));

            return "redirect:/usuarios/verPerfil";

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return "redirect:/usuarios/verPerfil";

        }
    }

    // Ver Vista para Cambiar la Contrasena
    @GetMapping("/usuarios/cambiarContrasena")
    public String verCambiarContrasena(Model modelo,
            Authentication auth) {

        List<Categoria> categorias = categoriaService.obtenerTodo();

        modelo.addAttribute("categorias", categorias);
        modelo.addAttribute("usuario", (Usuario) auth.getPrincipal());

        return "perfil/cambiarContrasena";
    }

    // Cambiar la Contrasena
    @PostMapping("/usuarios/cambiarContrasena/{id}")
    public String cambiarContrasena(@PathVariable int id,
            @RequestParam(name = "actual") String actual,
            @RequestParam(name = "nueva") String nueva,
            @RequestParam(name = "nuevaRepetida") String nuevaRepetida) {
        try {

            Usuario usuario = usuarioService.buscarPorId(id).orElse(null);

            if (usuario == null) {
                throw new IllegalArgumentException("Error! El usuario ingresado no existe.");
            }

            if (!passwordEncoder.matches(actual, usuario.getPassword())) {
                System.out.println("no coincide");
                throw new BadCredentialsException("La contraseña proporcionada no coincide con la registrada.");
            }
 
            if (!nueva.equals(nuevaRepetida)) {
                throw new IllegalArgumentException("Las contraseñas no coinciden.");
            }

            usuario.setPassword(passwordEncoder.encode(nueva));

            usuario = usuarioService.modificar(usuario);

            SecurityContextHolder.getContext()
                    .setAuthentication(
                            new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()));

            return "redirect:/usuarios/cambiarContrasena";

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return "redirect:/usuarios/cambiarContrasena";

        }
    }
}
