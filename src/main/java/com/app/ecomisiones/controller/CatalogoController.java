package com.app.ecomisiones.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.service.Categoria.CategoriaServiceImpl;

@Controller
@RequestMapping("/catalogo")
public class CatalogoController {

    @Autowired
    private CategoriaServiceImpl categoriaService;

    @GetMapping("/ver")
    public String login(Model model, Authentication auth) {

// Lista de categorías y productos desde la base de datos
        Usuario usuario = (Usuario) auth.getPrincipal();

        List<Categoria> cat = categoriaService.obtenerTodo();
        Map<Categoria, List<Categoria>> categorias = new HashMap<>();
        for (Categoria categoria : cat) {
            if (categoria.getPadre() == null) {
                List<Categoria> subcategorias = new ArrayList<>();
                for (Categoria subcategoria : categoria.getSubcategorias()) {
                    subcategorias.add(subcategoria);
                }
                categorias.put(categoria, subcategorias);
            }
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("categorias", categorias);
        return "catalogo";
    }

    /*
     * @GetMapping("/logout")
     * public String logout(HttpSession session){
     * session.removeAttribute("usuario");
     * return "login";
     * }
     */

    /*
     * @PostMapping("/login")
     * public String login(
     * 
     * @RequestParam("correo") String correo,
     * 
     * @RequestParam("password") String password,
     * Model model, HttpSession session) {
     * 
     * 
     * // Verificamos si existe un estudiante con el DNI y la contraseña
     * Optional<Usuario> estudianteOpt = usuarioService.iniciarSesion(correo,
     * password);
     * 
     * if (estudianteOpt.isPresent()) {
     * // Si el estudiante existe, generamos un token de sesión
     * String token = UUID.randomUUID().toString();
     * session.setAttribute("token", token);
     * 
     * // Agregamos el token al modelo para que pueda ser utilizado en la vista
     * model.addAttribute("token", token);
     * model.addAttribute("nombre", estudianteOpt.toString());
     * 
     * return "index"; // Página a la que se redirige después de iniciar sesión
     * } else {
     * model.addAttribute("error", "Correo o contraseña incorrectos.");
     * return "login";
     * }
     * }
     */
}
