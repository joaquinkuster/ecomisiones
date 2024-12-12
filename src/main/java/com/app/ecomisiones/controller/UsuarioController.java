package com.app.ecomisiones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.MedioDePago;
import com.app.ecomisiones.model.Sucursal;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.service.Categoria.CategoriaServiceImpl;
import com.app.ecomisiones.service.MedioDePago.MedioDePagoServiceImpl;
import com.app.ecomisiones.service.Sucursal.SucursalServiceImpl;
import com.app.ecomisiones.service.Usuario.UsuarioServiceImpl;

/**
 * Controlador encargado de gestionar las acciones relacionadas con los usuarios
 * tales como la visualización y modificación del perfil y el cambio de contraseña.
 * También se gestionan las categorías, sucursales y medios de pago disponibles para el usuario.
 */
@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private CategoriaServiceImpl categoriaService;

    @Autowired
    private SucursalServiceImpl sucursalService;

    @Autowired
    private MedioDePagoServiceImpl medioDePagoService;

    // Usamos BCryptPasswordEncoder para gestionar las contraseñas de manera segura.
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Muestra el perfil del usuario autenticado, con las categorías, sucursales y medios de pago disponibles.
     *
     * @param modelo Objeto Model que se pasa a la vista.
     * @param auth Objeto Authentication que contiene la información del usuario autenticado.
     * @return La vista que muestra el perfil del usuario.
     */
    @GetMapping("/verPerfil")
    public String verPerfil(Model modelo, Authentication auth) {

        // Obtener las categorías, sucursales y medios de pago para mostrar en la vista.
        List<Categoria> categorias = categoriaService.obtenerTodo();
        List<Sucursal> sucursales = sucursalService.obtenerTodo();
        List<MedioDePago> mediosDePagos = medioDePagoService.obtenerTodo();

        modelo.addAttribute("categorias", categorias);
        modelo.addAttribute("sucursales", sucursales);
        modelo.addAttribute("mediosDePagos", mediosDePagos);
        modelo.addAttribute("usuario", (Usuario) auth.getPrincipal());
        modelo.addAttribute("titulo", "Mi perfil");

        return "pages/perfil/verPerfil";
    }

    /**
     * Permite modificar los datos del perfil del usuario.
     *
     * @param id El ID del usuario a modificar.
     * @param nombre Nuevo nombre del usuario.
     * @param apellido Nuevo apellido del usuario.
     * @param correo Nuevo correo electrónico del usuario.
     * @param telefono Nuevo número de teléfono del usuario (opcional).
     * @param idSucursal ID de la sucursal asociada al usuario (opcional).
     * @param idMedioPago ID del medio de pago predeterminado del usuario (opcional).
     * @param redirectAttributes Atributos de redirección para mostrar mensajes.
     * @return Redirige a la vista del perfil con el mensaje correspondiente.
     */
    @PostMapping("/modificar/{id}")
    public String modificarPerfil(@PathVariable int id,
                                  @RequestParam(name = "nombre") String nombre,
                                  @RequestParam(name = "apellido") String apellido,
                                  @RequestParam(name = "correo") String correo,
                                  @RequestParam(name = "telefono", required = false) String telefono,
                                  @RequestParam(name = "sucursal", required = false) Integer idSucursal,
                                  @RequestParam(name = "medioPago", required = false) Integer idMedioPago,
                                  RedirectAttributes redirectAttributes) {

        try {

            // Buscar al usuario por ID.
            Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
            if (usuario == null) {
                throw new IllegalArgumentException("Error! El usuario ingresado no existe.");
            }

            // Actualizar los atributos del usuario.
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

            if (idMedioPago != null) {
                MedioDePago medioDePago = medioDePagoService.buscarPorId(idMedioPago).orElse(null);
                if (medioDePago == null) {
                    throw new IllegalArgumentException("Error! El medio de pago ingresado no existe.");
                } else {
                    usuario.setMedioDePagoPredeterminado(medioDePago);
                }
            }

            // Guardar los cambios en el usuario.
            usuario = usuarioService.modificar(usuario);

            // Actualizar el contexto de autenticación con los nuevos datos del usuario.
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()));

            redirectAttributes.addFlashAttribute("mensaje", "Hecho! Tu perfil ha sido actualizado correctamente.");
            return "redirect:/usuario/verPerfil";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/usuario/verPerfil";

        }
    }

    /**
     * Muestra la vista para cambiar la contraseña del usuario autenticado.
     *
     * @param modelo Objeto Model que se pasa a la vista.
     * @param auth Objeto Authentication que contiene la información del usuario autenticado.
     * @return La vista para cambiar la contraseña.
     */
    @GetMapping("/cambiarContrasena")
    public String verFormularioCambiarContrasena(Model modelo, Authentication auth) {

        // Obtener las categorías para mostrar en la vista.
        List<Categoria> categorias = categoriaService.obtenerTodo();

        modelo.addAttribute("categorias", categorias);
        modelo.addAttribute("usuario", (Usuario) auth.getPrincipal());
        modelo.addAttribute("titulo", "Cambiar mi contraseña");

        return "pages/perfil/cambiarContrasena";
    }

    /**
     * Permite al usuario cambiar su contraseña.
     *
     * @param id El ID del usuario cuyo password será modificado.
     * @param actual La contraseña actual proporcionada por el usuario.
     * @param nueva La nueva contraseña que el usuario quiere establecer.
     * @param nuevaRepetida Confirmación de la nueva contraseña.
     * @param redirectAttributes Atributos de redirección para mostrar mensajes.
     * @return Redirige a la vista de cambio de contraseña con el mensaje correspondiente.
     */
    @PostMapping("/cambiarContrasena/{id}")
    public String cambiarContrasena(@PathVariable int id,
                                    @RequestParam(name = "actual") String actual,
                                    @RequestParam(name = "nueva") String nueva,
                                    @RequestParam(name = "nuevaRepetida") String nuevaRepetida,
                                    RedirectAttributes redirectAttributes) {

        try {

            // Buscar al usuario por ID.
            Usuario usuario = usuarioService.buscarPorId(id).orElse(null);

            if (usuario == null) {
                throw new IllegalArgumentException("Error! El usuario ingresado no existe.");
            }

            // Verificar que la contraseña actual coincida con la registrada.
            if (!passwordEncoder.matches(actual, usuario.getPassword())) {
                throw new BadCredentialsException("La contraseña proporcionada no coincide con la registrada.");
            }

            // Verificar que las contraseñas nuevas coincidan.
            if (!nueva.equals(nuevaRepetida)) {
                throw new IllegalArgumentException("Las contraseñas no coinciden.");
            }

            // Actualizar la contraseña del usuario.
            usuario.setPassword(passwordEncoder.encode(nueva));

            // Guardar los cambios en el usuario.
            usuario = usuarioService.modificar(usuario);

            // Actualizar el contexto de autenticación con los nuevos datos del usuario.
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()));

            redirectAttributes.addFlashAttribute("mensaje", "Hecho! Tu contraseña ha sido actualizada correctamente.");
            return "redirect:/usuario/cambiarContrasena";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/usuario/cambiarContrasena";

        }
    }
}
