package com.app.ecomisiones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.ecomisiones.model.*;
import com.app.ecomisiones.service.Categoria.CategoriaServiceImpl;
import com.app.ecomisiones.service.Producto.ProductoServiceImpl;
import com.app.ecomisiones.utils.Utilidades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con los productos en el sistema ecomarket.
 * Este controlador maneja la visualización de productos, la búsqueda de productos por diferentes criterios
 * como stock, fecha de lanzamiento, ventas, etc., y la filtración de productos según diversos parámetros.
 */
@Controller
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoServiceImpl productoService;

    @Autowired
    private CategoriaServiceImpl categoriaService;

    /**
     * Muestra los detalles de un producto específico.
     *
     * @param id El ID del producto que se desea visualizar.
     * @param modelo El modelo para pasar los atributos a la vista.
     * @param auth La autenticación del usuario.
     * @param redirectAttributes Atributos para redirigir con mensaje de error.
     * @return La vista del producto con sus detalles y productos relacionados.
     */
    @GetMapping("/ver/{id}")
    public String verProducto(@PathVariable int id, Model modelo, Authentication auth,
            RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = (Usuario) auth.getPrincipal(); // Obtener el usuario autenticado
            List<Categoria> categorias = categoriaService.obtenerTodo(); // Obtener todas las categorías

            // Buscar el producto por su ID
            Producto producto = productoService.buscarPorId(id).orElse(null);
            if (producto == null) {
                throw new IllegalArgumentException("Error! El producto ingresado no existe.");
            }

            // Obtener productos relacionados de la misma categoría
            List<Producto> relacionados = Utilidades
                    .limitar(productoService.buscarPorCategoria(producto.getCategoria()), 5);

            // Convertir las imágenes del producto a formato Base64
            List<String> imgPrincipales = Utilidades.convertir(producto);

            // Crear un mapa con productos relacionados y sus imágenes
            Map<Producto, List<String>> relacionados2 = cargarProductosConImagenes(relacionados);

            // Verificar si el producto ya está en el carrito del usuario
            Set<DetalleCarrito> detalles = usuario.getCarrito().getDetalles();
            boolean estaEnCarrito = detalles.stream()
                    .anyMatch(detalle -> detalle.getProducto().getId() == producto.getId());

            // Agregar los atributos necesarios a la vista
            modelo.addAttribute("producto", producto);
            modelo.addAttribute("estaEnCarrito", estaEnCarrito);
            modelo.addAttribute("imagenes", imgPrincipales);
            modelo.addAttribute("relacionados", relacionados2);
            modelo.addAttribute("categorias", categorias);
            modelo.addAttribute("usuario", usuario);
            modelo.addAttribute("titulo", producto);

            return "pages/vistaProducto"; // Vista del producto

        } catch (Exception e) {
            // Manejo de excepciones, se agrega mensaje y se redirige
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";
        }
    }

    /**
     * Muestra los productos más recientes con stock disponible.
     *
     * @param modelo El modelo para pasar los atributos a la vista.
     * @param auth La autenticación del usuario.
     * @param redirectAttributes Atributos para redirigir con mensaje de error.
     * @return La vista de productos más recientes.
     */
    @GetMapping("/buscarUltimosDisponibles")
    public String buscarUltimosDisponibles(Model modelo,
            Authentication auth, RedirectAttributes redirectAttributes) {

        try {
            // Obtener categorías para la barra lateral
            Map<Categoria, List<Categoria>> categorias = Utilidades.buscarCategorias(categoriaService.obtenerTodo());
            modelo.addAttribute("categorias", categorias);

            // Obtener los productos más recientes con stock disponible
            List<Producto> filtr = productoService.buscarUltimosDisponibles();
            Map<Producto, List<String>> filtrados = cargarProductosConImagenes(filtr);

            // Pasar atributos a la vista
            modelo.addAttribute("usuario", (Usuario) auth.getPrincipal());
            modelo.addAttribute("filtrados", filtrados);
            modelo.addAttribute("busqueda", "Últimos disponibles");
            modelo.addAttribute("titulo", "Últimos disponibles");

            return "pages/busqueda"; // Vista de búsqueda

        } catch (Exception e) {
            // Manejo de excepciones
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";
        }
    }

    /**
     * Muestra los productos más recientes lanzados en el sistema.
     *
     * @param modelo El modelo para pasar los atributos a la vista.
     * @param auth La autenticación del usuario.
     * @param redirectAttributes Atributos para redirigir con mensaje de error.
     * @return La vista de productos más recientes lanzados.
     */
    @GetMapping("/buscarRecientes")
    public String buscarRecientes(Model modelo,
            Authentication auth, RedirectAttributes redirectAttributes) {

        try {
            // Obtener categorías para la barra lateral
            Map<Categoria, List<Categoria>> categorias = Utilidades.buscarCategorias(categoriaService.obtenerTodo());
            modelo.addAttribute("categorias", categorias);

            // Obtener los productos más recientes
            List<Producto> filtr = productoService.buscarRecientes();
            Map<Producto, List<String>> filtrados = cargarProductosConImagenes(filtr);

            // Pasar atributos a la vista
            modelo.addAttribute("usuario", (Usuario) auth.getPrincipal());
            modelo.addAttribute("filtrados", filtrados);
            modelo.addAttribute("busqueda", "Últimos lanzamientos");
            modelo.addAttribute("titulo", "Últimos lanzamientos");

            return "pages/busqueda"; // Vista de búsqueda

        } catch (Exception e) {
            // Manejo de excepciones
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";
        }
    }

    /**
     * Muestra los productos más vendidos.
     *
     * @param modelo El modelo para pasar los atributos a la vista.
     * @param auth La autenticación del usuario.
     * @param redirectAttributes Atributos para redirigir con mensaje de error.
     * @return La vista de productos más vendidos.
     */
    @GetMapping("/buscarMasVendidos")
    public String buscarMasVendidos(Model modelo,
            Authentication auth, RedirectAttributes redirectAttributes) {

        try {
            // Obtener categorías para la barra lateral
            Map<Categoria, List<Categoria>> categorias = Utilidades.buscarCategorias(categoriaService.obtenerTodo());
            modelo.addAttribute("categorias", categorias);

            // Obtener los productos más vendidos
            List<Producto> filtr = productoService.buscarMasVendidos();
            Map<Producto, List<String>> filtrados = cargarProductosConImagenes(filtr);

            // Pasar atributos a la vista
            modelo.addAttribute("usuario", (Usuario) auth.getPrincipal());
            modelo.addAttribute("filtrados", filtrados);
            modelo.addAttribute("busqueda", "Productos destacados");
            modelo.addAttribute("titulo", "Productos destacados");

            return "pages/busqueda"; // Vista de búsqueda

        } catch (Exception e) {
            // Manejo de excepciones
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";
        }
    }

    /**
     * Permite filtrar productos según diferentes criterios como nombre, precio, descuento, o categoría.
     *
     * @param busqueda El término de búsqueda por nombre o descripción del producto.
     * @param idCategoria El ID de la categoría para filtrar productos.
     * @param precio El precio mínimo para filtrar productos.
     * @param descuento El descuento máximo para filtrar productos.
     * @param redirectAttributes Atributos para redirigir con mensaje de error.
     * @param modelo El modelo para pasar los atributos a la vista.
     * @param auth La autenticación del usuario.
     * @return La vista con los productos filtrados.
     */
    @GetMapping("/filtrar")
    public String filtrarProductos(
            @RequestParam(required = false) String busqueda,
            @RequestParam(name = "categoria", required = false) Integer idCategoria,
            @RequestParam(required = false) Float precio,
            @RequestParam(required = false) Float descuento,
            RedirectAttributes redirectAttributes,
            Model modelo, Authentication auth) {
        try {
            // Obtener categorías para la barra lateral
            Map<Categoria, List<Categoria>> categorias = Utilidades.buscarCategorias(categoriaService.obtenerTodo());
            modelo.addAttribute("categorias", categorias);

            // Filtrar productos según los parámetros dados
            List<Producto> filtr = filtrar(busqueda, precio, descuento, idCategoria);
            Map<Producto, List<String>> filtrados = cargarProductosConImagenes(filtr);

            // Pasar atributos a la vista
            modelo.addAttribute("usuario", (Usuario) auth.getPrincipal());
            modelo.addAttribute("filtrados", filtrados);
            if (idCategoria != null) {
                modelo.addAttribute("categoria", categoriaService.buscarPorId(idCategoria).orElse(null));
            }
            modelo.addAttribute("precio", precio);
            modelo.addAttribute("descuento", descuento);
            modelo.addAttribute("busqueda", busqueda != null && !busqueda.isEmpty() ? busqueda : "Todos los productos");
            modelo.addAttribute("titulo", busqueda != null && !busqueda.isEmpty() ? busqueda : "Todos los productos");

            return "pages/busqueda"; // Vista de búsqueda

        } catch (Exception e) {
            // Manejo de excepciones
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";
        }
    }

    /**
     * Filtra los productos en función de los parámetros dados.
     *
     * @param nombre El nombre o parte del nombre del producto.
     * @param precio El precio mínimo de los productos.
     * @param descuento El descuento máximo de los productos.
     * @param idCategoria El ID de la categoría del producto.
     * @return Lista de productos filtrados.
     */
    private List<Producto> filtrar(String nombre, Float precio, Float descuento, Integer idCategoria) {
        List<Producto> productos = productoService.obtenerTodo();
        List<Producto> filtrados = new ArrayList<>();

        // Filtrar productos según los criterios especificados
        for (Producto producto : productos) {
            if (((nombre == null || producto.getNombre().toLowerCase().startsWith(nombre))
                    || (nombre == null || producto.getDescripcion().toLowerCase().contains(nombre)))
                    && (precio == null || precio >= producto.getPrecio())
                    && (descuento == null || descuento <= producto.getDescuento())
                    && (idCategoria == null || producto.getCategoria().getId() == idCategoria)) {
                filtrados.add(producto);
            }
        }

        return filtrados;
    }

    /**
     * Carga los productos con sus imágenes convertidas a formato Base64.
     *
     * @param productos Lista de productos a procesar.
     * @return Mapa que asocia a cada producto con una lista de sus imágenes.
     */
    private Map<Producto, List<String>> cargarProductosConImagenes(List<Producto> productos) {
        Map<Producto, List<String>> productosConImagenes = new HashMap<>();
        for (Producto producto : productos) {
            productosConImagenes.put(producto, Utilidades.convertir(producto));
        }
        return productosConImagenes;
    }
}
