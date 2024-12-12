package com.app.ecomisiones.utils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Producto;

/**
 * Clase de utilidades que contiene métodos estáticos utilizados en diversas operaciones
 * relacionadas con productos y categorías.
 */
public class Utilidades {

    /**
     * Convierte las imágenes de un producto a su representación en Base64.
     * 
     * @param producto El producto que contiene las imágenes.
     * @return Una lista de cadenas en formato Base64 representando las imágenes del producto.
     */
    public static List<String> convertir(Producto producto) {
        // Lista para almacenar las imágenes codificadas en Base64
        List<String> img = new ArrayList<>();

        // Codifica cada imagen en Base64 y agrega a la lista
        img.addAll(producto.getImagenes()
                .stream()
                .map(imagen -> Base64.getEncoder().encodeToString(imagen.getImagen()))
                .toList());

        return img;
    }

    /**
     * Limita el tamaño de una lista, devolviendo una sublista con un máximo de elementos
     * especificado. Si la lista tiene más elementos que el máximo, se devuelve una sublista
     * con los primeros elementos hasta el límite; si no, se devuelve la lista original.
     * 
     * @param lista La lista a limitar.
     * @param max El número máximo de elementos que se debe devolver.
     * @param <T> El tipo de los elementos de la lista.
     * @return Una sublista con un máximo de {@code max} elementos.
     */
    public static <T> List<T> limitar(List<T> lista, int max) {
        // Si la lista es mayor que el máximo, devuelve la sublista con los primeros 'max' elementos
        return lista.size() > max ? lista.subList(0, max) : lista;
    }

    /**
     * Organiza una lista de categorías en un mapa donde las categorías principales son las claves,
     * y las subcategorías de cada una son los valores correspondientes.
     * 
     * @param cat La lista de categorías a organizar.
     * @return Un mapa que asocia a cada categoría principal con sus subcategorías.
     */
    public static Map<Categoria, List<Categoria>> buscarCategorias(List<Categoria> cat) {
        // Mapa para almacenar las categorías principales y sus subcategorías
        Map<Categoria, List<Categoria>> categorias = new HashMap<>();

        // Itera sobre cada categoría
        for (Categoria categoria : cat) {
            // Si la categoría no tiene un padre (es una categoría principal)
            if (categoria.getPadre() == null) {
                // Lista para almacenar las subcategorías
                List<Categoria> subcategorias = new ArrayList<>();

                // Agrega las subcategorías de la categoría principal al mapa
                for (Categoria subcategoria : categoria.getSubcategorias()) {
                    // Se puede agregar un registro de debug si es necesario para inspeccionar las subcategorías
                    // System.out.println(subcategoria); 
                    subcategorias.add(subcategoria);
                }

                // Se agrega la categoría principal junto con sus subcategorías al mapa
                categorias.put(categoria, subcategorias);
            }
        }

        return categorias;
    }
}
