package com.app.ecomisiones.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

/**
 * Controlador para gestionar la visualización del catálogo de productos.
 * 
 * Muestra las categorías de productos, agrupadas por su jerarquía y ordenadas alfabéticamente.
 */
@Controller
@RequestMapping("/catalogo")
public class CatalogoController {

    @Autowired
    private CategoriaServiceImpl categoriaService;

    /**
     * Método para mostrar el catálogo de productos.
     * 
     * Obtiene las categorías desde la base de datos, las agrupa por jerarquía y las ordena alfabéticamente.
     * 
     * @param model El modelo que se utiliza para pasar datos a la vista.
     * @param auth  La autenticación del usuario que está visualizando el catálogo.
     * @return El nombre de la vista que muestra el catálogo de productos.
     */
    @GetMapping("/ver")
    public String verCatalogo(Model model, Authentication auth) {

        // Obtiene el usuario autenticado
        Usuario usuario = (Usuario) auth.getPrincipal();

        // Obtiene todas las categorías desde la base de datos
        List<Categoria> categoriasList = categoriaService.obtenerTodo();
        
        // Mapa para almacenar categorías y sus respectivas subcategorías
        Map<Categoria, List<Categoria>> categorias = new HashMap<>();
        
        // Agrupa las categorías en un mapa, donde la clave es la categoría principal y el valor es su lista de subcategorías
        for (Categoria categoria : categoriasList) {
            if (categoria.getPadre() == null) { // Si la categoría no tiene un padre, es una categoría principal
                List<Categoria> subcategorias = new ArrayList<>();
                
                // Agrega las subcategorías de esta categoría principal
                for (Categoria subcategoria : categoria.getSubcategorias()) {
                    subcategorias.add(subcategoria);
                }
                
                // Coloca la categoría principal y sus subcategorías en el mapa
                categorias.put(categoria, subcategorias);
            }
        }

        // Añade el usuario y el mapa de categorías al modelo para ser utilizados en la vista
        model.addAttribute("usuario", usuario);
        
        // Ordena las claves del mapa de categorías alfabéticamente por nombre
        List<Categoria> sortedKeys = new ArrayList<>(categorias.keySet());
        sortedKeys.sort(Comparator.comparing(Categoria::getNombre));

        // Crea un nuevo mapa con las categorías ordenadas
        Map<Categoria, List<Categoria>> sortedCategorias = new LinkedHashMap<>();
        for (Categoria key : sortedKeys) {
            sortedCategorias.put(key, categorias.get(key));
        }

        // Añade las categorías ordenadas al modelo
        model.addAttribute("categorias", sortedCategorias);
        
        // Título de la vista
        model.addAttribute("titulo", "Catálogo de productos");

        // Retorna el nombre de la vista que se utilizará para renderizar el catálogo
        return "pages/catalogo";
    }
}
