package com.app.ecomisiones.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.ecomisiones.service.Categoria.CategoriaServiceImpl;
import com.app.ecomisiones.service.Producto.ProductoServiceImpl;
import com.app.ecomisiones.utils.Utilidades;
import com.app.ecomisiones.model.Categoria;
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

        List<Producto> dest = Utilidades.limitar(productoService.buscarMasVendidos(), 5);
        Map<Producto, List<String>> destacados = new HashMap<>();
        for (Producto producto : dest) {
            destacados.put(producto, Utilidades.convertir(producto));
        }

        List<Producto> rec = Utilidades.limitar(productoService.buscarRecientes(), 5);
        Map<Producto, List<String>> recientes = new HashMap<>();
        for (Producto producto : rec) {
            recientes.put(producto, Utilidades.convertir(producto));
        }

        model.addAttribute("categorias", categorias);
        model.addAttribute("destacados", destacados);
        model.addAttribute("recientes", recientes);
        model.addAttribute("usuario", usuario);

        return "index";
    }

    @GetMapping("/acercaDe")
    public String acercadE(Model model, Authentication auth) {
        model.addAttribute("usuario", (Usuario) auth.getPrincipal());
        return "acercaDe";
    }

    @GetMapping("/error")
    public String error(Model model, Authentication auth) {
        model.addAttribute("usuario", (Usuario) auth.getPrincipal());
        return "error";
    }
}