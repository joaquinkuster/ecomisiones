package com.app.ecomisiones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.ecomisiones.service.Categoria.CategoriaServiceImpl;
import com.app.ecomisiones.service.Producto.ProductoServiceImpl;
import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Producto;

@Controller
public class InicioController {

    @Autowired
    @Lazy
    private CategoriaServiceImpl categoriaService;

    @Autowired
    @Lazy
    private ProductoServiceImpl productoService;

    @GetMapping("/inicio")
    public String home(Model model) {
        // Lista de categorías y productos desde la base de datos
        List<Categoria> categorias = categoriaService.obtenerTodo();
        List<Producto> productos = productoService.obtenerTodo();

        model.addAttribute("categorias", categorias);
        model.addAttribute("productos", productos);
        return "index";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String admin(Model model) {
        model.addAttribute("contenidoAdmin", "/admin/home");
        return "admin/adminPage";
    }
}