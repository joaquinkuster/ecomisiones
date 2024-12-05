package com.app.ecomisiones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.ecomisiones.model.Carrito;
import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.MedioDePago;
import com.app.ecomisiones.model.Sucursal;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.service.Carrito.CarritoServiceImpl;
import com.app.ecomisiones.service.Categoria.CategoriaServiceImpl;
import com.app.ecomisiones.service.MedioDePago.MedioDePagoServiceImpl;
import com.app.ecomisiones.service.Sucursal.SucursalServiceImpl;
import com.app.ecomisiones.service.Usuario.UsuarioServiceImpl;

import jakarta.validation.Valid;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private CategoriaServiceImpl categoriaService;

    @Autowired
    private CarritoServiceImpl carritoService;

    @Autowired
    private SucursalServiceImpl sucursalService;

    @Autowired
    private MedioDePagoServiceImpl medioDePagoService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final AuthenticationManager authenticationManager;

    public UsuarioController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

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

            // Redirigir al usuario a la página de inicio o donde se desee

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return "registro";

        }
    }

    // Ver Perfil
    @GetMapping("/usuarios/verPerfil")
    public String verPerfil(Model modelo,
            Authentication auth) {

        List<Categoria> categorias = categoriaService.obtenerTodo();
        List<Sucursal> sucursales = sucursalService.obtenerTodo();
        List<MedioDePago> mediosDePagos = medioDePagoService.obtenerTodo();

        modelo.addAttribute("categorias", categorias);
        modelo.addAttribute("sucursales", sucursales);
        modelo.addAttribute("mediosDePagos", mediosDePagos);
        modelo.addAttribute("usuario", (Usuario) auth.getPrincipal());

        return "perfil/verPerfil";
    }

    // Modificar un Perfil
    @PostMapping("/usuarios/modificar/{id}")
    public String modificarPerfil(@PathVariable int id,
            @RequestParam(name = "nombre") String nombre,
            @RequestParam(name = "apellido") String apellido,
            @RequestParam(name = "correo") String correo,
            @RequestParam(name = "telefono", required = false) String telefono,
            @RequestParam(name = "sucrusal", required = false) Integer idSucursal,
            @RequestParam(name = "medioDePago", required = false) Integer idMedioDePago,
            RedirectAttributes redirectAttributes) {

        try {

            Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
            if (usuario == null) {
                throw new IllegalArgumentException("Error! El usuario ingresado no existe.");
            }

            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setCorreo(correo);

            if (telefono != null) {
                usuario.setTelefono(telefono);
            }

            if (idSucursal != null) {
                Sucursal sucursal = sucursalService.buscarPorId(idSucursal).orElse(null);
                if (sucursal == null) {
                    throw new IllegalArgumentException("Error! La sucursal ingresada no existe.");
                } else {
                    usuario.setSucursalMasCercana(sucursal);
                }
            }

            if (idMedioDePago != null) {
                MedioDePago medioDePago = medioDePagoService.buscarPorId(idSucursal).orElse(null);
                if (medioDePago == null) {
                    throw new IllegalArgumentException("Error! El medio de pago ingresado no existe.");
                } else {
                    usuario.setMedioDePagoPredeterminado(medioDePago);
                }
            }

            usuario = usuarioService.modificar(usuario);

            SecurityContextHolder.getContext()
                    .setAuthentication(
                            new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()));

            return "redirect:/usuarios/verPerfil";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
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
