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
 * Clase controlador para los productos en ecomarket.
 */

@Controller
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoServiceImpl productoService;

    @Autowired
    private CategoriaServiceImpl categoriaService;

    // Mostrar detalles del producto
    @GetMapping("/ver/{id}")
    public String verMercaderia(@PathVariable int id, Model modelo, Authentication auth,
            RedirectAttributes redirectAttributes) {
        try {

            Usuario usuario = (Usuario) auth.getPrincipal();
            List<Categoria> categorias = categoriaService.obtenerTodo();

            Producto producto = productoService.buscarPorId(id).orElse(null);
            if (producto == null) {
                throw new IllegalArgumentException("Error! El producto ingresado no existe.");
            }

            List<Producto> relacionados = Utilidades.limitar(productoService.buscarPorCategoria(producto.getCategoria()), 5);

            // Convertir las imágenes a Base64
            List<String> imgPrincipales = Utilidades.convertir(producto);

            Map<Producto, List<String>> relacionados2 = cargarProductosConImagenes(relacionados);

            Set<DetalleCarrito> detalles = usuario.getCarrito().getDetalles();
            boolean estaEnCarrito = false;
            if (!detalles.isEmpty()) {
                for (DetalleCarrito detalle : detalles) {
                    if (detalle.getProducto().getId() == producto.getId()) {
                        estaEnCarrito = true;
                    }
                }
            }

            modelo.addAttribute("producto", producto);
            modelo.addAttribute("estaEnCarrito", estaEnCarrito);
            modelo.addAttribute("imagenes", imgPrincipales);
            modelo.addAttribute("relacionados", relacionados2);
            modelo.addAttribute("categorias", categorias);
            modelo.addAttribute("usuario", usuario);

            return "vistaProducto";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";

        }
    }

    @GetMapping("/buscarUltimosDisponibles")
    public String buscarPorStock(Model modelo,
            Authentication auth, RedirectAttributes redirectAttributes) {

        try {

            Map<Categoria, List<Categoria>> categorias = buscarCategorias();
            modelo.addAttribute("categorias", categorias);

            List<Producto> filtr = productoService.buscarUltimosDisponibles();
            Map<Producto, List<String>> filtrados = cargarProductosConImagenes(filtr);
            modelo.addAttribute("usuario", (Usuario) auth.getPrincipal());
            modelo.addAttribute("filtrados", filtrados);
            return "busqueda";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";
        }
    }

    @GetMapping("/buscarRecientes")
    public String buscarRecientes(Model modelo,
            Authentication auth, RedirectAttributes redirectAttributes) {

        try {

            Map<Categoria, List<Categoria>> categorias = buscarCategorias();
            modelo.addAttribute("categorias", categorias);

            List<Producto> filtr = productoService.buscarRecientes();
            Map<Producto, List<String>> filtrados = cargarProductosConImagenes(filtr);
            modelo.addAttribute("usuario", (Usuario) auth.getPrincipal());
            modelo.addAttribute("filtrados", filtrados);
            return "busqueda";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";
        }
    }

    @GetMapping("/buscarMasVendidos")
    public String buscarRebuscarMasVendidoscientes(Model modelo,
            Authentication auth, RedirectAttributes redirectAttributes) {

        try {

            Map<Categoria, List<Categoria>> categorias = buscarCategorias();
            modelo.addAttribute("categorias", categorias);

            List<Producto> filtr = productoService.buscarMasVendidos();
            Map<Producto, List<String>> filtrados = cargarProductosConImagenes(filtr);
            modelo.addAttribute("usuario", (Usuario) auth.getPrincipal());
            modelo.addAttribute("filtrados", filtrados);
            return "busqueda";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";
        }
    }

    @GetMapping("/filtrar")
    public String filtrar(
            @RequestParam(required = false) String nombre,
            @RequestParam(name = "categoria", required = false) Integer idCategoria,
            @RequestParam(required = false) Float precio,
            @RequestParam(required = false) Float descuento,
            RedirectAttributes redirectAttributes,
            Model modelo, Authentication auth) {
        try {

            Map<Categoria, List<Categoria>> categorias = buscarCategorias();
            modelo.addAttribute("categorias", categorias);

            List<Producto> filtr = filtrar(nombre, precio, descuento, idCategoria);
            Map<Producto, List<String>> filtrados = cargarProductosConImagenes(filtr);
            modelo.addAttribute("usuario", (Usuario) auth.getPrincipal());
            modelo.addAttribute("filtrados", filtrados);
            return "busqueda";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";
        }
    }

    private List<Producto> filtrar(String nombre, Float precio, Float descuento, Integer idCategoria) {
        List<Producto> productos = productoService.obtenerTodo();
        List<Producto> filtrados = new ArrayList<>();

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

    private Map<Categoria, List<Categoria>> buscarCategorias() {
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
        return categorias;
    }

    private Map<Producto, List<String>> cargarProductosConImagenes(List<Producto> productos) {
        Map<Producto, List<String>> productosConImagenes = new HashMap<>();
        for (Producto producto : productos) {
            productosConImagenes.put(producto, Utilidades.convertir(producto));
        }
        return productosConImagenes;
    }
}
