package com.app.ecomisiones.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.ecomisiones.service.Categoria.CategoriaServiceImpl;
import com.app.ecomisiones.service.Producto.ProductoServiceImpl;
import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Imagen;
import com.app.ecomisiones.model.Producto;
import com.app.ecomisiones.model.Usuario;

import org.springframework.security.core.Authentication;


@Controller
public class InicioController {

    @Autowired
    private CategoriaServiceImpl categoriaService;

    @Autowired
    private ProductoServiceImpl productoService;

    @GetMapping("/inicio")
    public String home(Model model, Authentication auth) {
        // Lista de categorías y productos desde la base de datos
        List<Categoria> categorias = categoriaService.obtenerTodo();
        List<Producto> productos = productoService.obtenerTodo();
        Usuario usuario = (Usuario) auth.getPrincipal();

        Map<Producto, List<String>> productosConImagenes = new HashMap<>();

        for (Producto producto : productos) {
            List<String> imagenesBase64 = new ArrayList<>();
            for (Imagen imagen : producto.getImagenes()) {
                imagenesBase64.add(Base64.getEncoder().encodeToString(imagen.getImagen())); 
            }
            productosConImagenes.put(producto, imagenesBase64);
        }


        model.addAttribute("categorias", categorias);
        model.addAttribute("productos", productosConImagenes);
        model.addAttribute("usuario", usuario);
        return "index";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('Administrador')")
    public String admin(Model model, Authentication auth) {
        List<Categoria> categorias = categoriaService.obtenerTodo();

        model.addAttribute("categorias", categorias);
        model.addAttribute("usuario", (Usuario) auth.getPrincipal());
        return "admin/admin";
    }
}