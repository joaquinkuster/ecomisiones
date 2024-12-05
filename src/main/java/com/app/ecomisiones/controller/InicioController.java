package com.app.ecomisiones.controller;

import java.util.ArrayList;
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
import com.app.ecomisiones.model.Producto;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.helper.Img;

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
        Usuario usuario = (Usuario) auth.getPrincipal();

        List<Categoria> cat = categoriaService.obtenerTodo();
        Map<Categoria, List<Categoria>> categorias = new HashMap<>();
        for (Categoria categoria : cat) {
            if (categoria.getPadre() == null) {
                List<Categoria> subcategorias = new ArrayList<>();
                for (Categoria subcategoria : categoria.getSubcategorias()) {
                    System.out.println(subcategoria);
                    subcategorias.add(subcategoria);
                }
                categorias.put(categoria, subcategorias);
            }
        }

        List<Producto> productos = productoService.obtenerTodo();
        Map<Producto, List<String>> productos2 = new HashMap<>();
        for (Producto producto : productos) {
            productos2.put(producto, Img.convertir(producto));
        }

        model.addAttribute("categorias", categorias);
        model.addAttribute("productos", productos2);
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