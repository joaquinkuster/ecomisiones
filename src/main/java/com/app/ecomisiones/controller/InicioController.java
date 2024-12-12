package com.app.ecomisiones.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.ecomisiones.service.Categoria.CategoriaServiceImpl;
import com.app.ecomisiones.service.Producto.ProductoServiceImpl;
import com.app.ecomisiones.utils.Utilidades;
import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Producto;
import com.app.ecomisiones.model.Usuario;

import org.springframework.security.core.Authentication;

/**
 * Controlador principal para gestionar la vista de inicio y las páginas relacionadas.
 * 
 * Este controlador maneja la lógica para mostrar la página de inicio, las categorías de productos,
 * productos destacados y recientes, y páginas adicionales como "Acerca de" y "Error".
 */
@Controller
public class InicioController {

    @Autowired
    private CategoriaServiceImpl categoriaService;

    @Autowired
    private ProductoServiceImpl productoService;

    /**
     * Método que maneja la vista de la página de inicio.
     * 
     * @param login Parámetro opcional que indica si el usuario acaba de iniciar sesión.
     * @param model El modelo que se utiliza para pasar datos a la vista.
     * @param auth  El objeto de autenticación que contiene la información del usuario actual.
     * @return El nombre de la vista "index", que muestra la página principal.
     */
    @GetMapping("/inicio")
    public String verInicio(@RequestParam(value = "login", required = false) String login, Model model,
            Authentication auth) {

        // Si el parámetro 'login' está presente, muestra un mensaje de inicio de sesión exitoso
        if (login != null) {
            model.addAttribute("mensaje", "Hecho! Has iniciado sesión correctamente.");
        }

        // Obtiene el usuario autenticado
        Usuario usuario = (Usuario) auth.getPrincipal();

        // Recupera todas las categorías de la base de datos
        List<Categoria> categoriasList = categoriaService.obtenerTodo();
        
        // Mapa para almacenar categorías principales y sus subcategorías
        Map<Categoria, List<Categoria>> categorias = new HashMap<>();
        
        // Itera sobre todas las categorías y agrupa las subcategorías debajo de su categoría principal
        for (Categoria categoria : categoriasList) {
            if (categoria.getPadre() == null) { // La categoría es principal si no tiene un padre
                List<Categoria> subcategorias = new ArrayList<>();
                
                // Agrega las subcategorías de la categoría principal
                for (Categoria subcategoria : categoria.getSubcategorias()) {
                    subcategorias.add(subcategoria);
                }
                
                // Añade la categoría principal y sus subcategorías al mapa
                categorias.put(categoria, subcategorias);
            }
        }

        // Limita la lista de productos más vendidos a los 5 primeros
        List<Producto> productosDestacados = Utilidades.limitar(productoService.buscarMasVendidos(), 5);
        
        // Mapa que asocia productos destacados con una lista de sus detalles (como imágenes, descripciones, etc.)
        Map<Producto, List<String>> destacados = new HashMap<>();
        for (Producto producto : productosDestacados) {
            destacados.put(producto, Utilidades.convertir(producto)); // Convierte los detalles del producto a una lista
        }

        // Limita la lista de productos recientes a los 5 primeros
        List<Producto> productosRecientes = Utilidades.limitar(productoService.buscarRecientes(), 5);
        
        // Mapa que asocia productos recientes con una lista de sus detalles
        Map<Producto, List<String>> recientes = new HashMap<>();
        for (Producto producto : productosRecientes) {
            recientes.put(producto, Utilidades.convertir(producto)); // Convierte los detalles del producto a una lista
        }

        // Agrega al modelo las categorías, productos destacados, recientes, y la información del usuario
        model.addAttribute("categorias", categorias);
        model.addAttribute("destacados", destacados);
        model.addAttribute("recientes", recientes);
        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Inicio");

        // Retorna la vista 'index' que será renderizada
        return "index";
    }

    /**
     * Método que maneja la vista "Acerca de", que describe la aplicación.
     * 
     * @param model El modelo utilizado para pasar datos a la vista.
     * @param auth  El objeto de autenticación que contiene la información del usuario actual.
     * @return El nombre de la vista "acercaDe", que muestra la página de información de la aplicación.
     */
    @GetMapping("/acercaDe")
    public String verAcercaDe(Model model, Authentication auth) {
        // Agrega el usuario al modelo y establece el título de la vista
        model.addAttribute("usuario", (Usuario) auth.getPrincipal());
        model.addAttribute("titulo", "Acerca de");

        // Retorna la vista 'acercaDe'
        return "pages/acercaDe";
    }

    /**
     * Método que maneja la vista de error en caso de que ocurra algún problema.
     * 
     * @param model El modelo utilizado para pasar datos a la vista.
     * @param auth  El objeto de autenticación que contiene la información del usuario actual.
     * @return El nombre de la vista "error", que muestra la página de error.
     */
    @GetMapping("/error")
    public String verError(Model model, Authentication auth) {
        // Agrega el usuario al modelo y establece el título de la vista
        model.addAttribute("usuario", (Usuario) auth.getPrincipal());
        model.addAttribute("titulo", "Error");

        // Retorna la vista 'error'
        return "pages/error";
    }
}
