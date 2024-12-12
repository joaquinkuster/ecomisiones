package com.app.ecomisiones.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.ecomisiones.model.Almacen;
import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Imagen;
import com.app.ecomisiones.model.Producto;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.service.Almacen.AlmacenServiceImpl;
import com.app.ecomisiones.service.Categoria.CategoriaServiceImpl;
import com.app.ecomisiones.service.Imagen.ImagenServiceImpl;
import com.app.ecomisiones.service.Pedido.PedidoServiceImpl;
import com.app.ecomisiones.service.Producto.ProductoServiceImpl;
import com.app.ecomisiones.utils.Utilidades;

/**
 * Controlador para la gestión del panel de administración.
 * Este controlador maneja las acciones relacionadas con la visualización y
 * gestión de productos, pedidos y almacenes por parte de los administradores.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductoServiceImpl productoService;

    @Autowired
    private CategoriaServiceImpl categoriaService;

    @Autowired
    private AlmacenServiceImpl almacenService;

    @Autowired
    private ImagenServiceImpl imagenService;

    @Autowired
    private PedidoServiceImpl pedidoService;

    /**
     * Muestra la página principal del administrador.
     * Solo accesible para usuarios con el rol de 'Administrador'.
     *
     * @param model el modelo para pasar datos a la vista.
     * @param auth  la autenticación del usuario.
     * @return la vista del panel de administración.
     */
    @GetMapping("")
    @PreAuthorize("hasRole('Administrador')")
    public String verAdmin(Model model, Authentication auth) {
        model.addAttribute("usuario", (Usuario) auth.getPrincipal());
        model.addAttribute("titulo", "Gestión de sistema");
        return "pages/admin/admin";
    }

    /**
     * Muestra la página con todos los pedidos.
     * Solo accesible para usuarios con el rol de 'Administrador'.
     *
     * @param model el modelo para pasar datos a la vista.
     * @param auth  la autenticación del usuario.
     * @return la vista de los pedidos.
     */
    @GetMapping("/verPedidos")
    @PreAuthorize("hasRole('Administrador')")
    public String verPedidos(Model model, Authentication auth) {
        model.addAttribute("usuario", (Usuario) auth.getPrincipal());
        model.addAttribute("pedidos", pedidoService.obtenerTodo());
        model.addAttribute("titulo", "Todos los pedidos");

        return "pages/admin/verPedidos";
    }

    /**
     * Muestra la página con todos los productos.
     * Solo accesible para usuarios con el rol de 'Administrador'.
     *
     * @param model el modelo para pasar datos a la vista.
     * @param auth  la autenticación del usuario.
     * @return la vista de los productos.
     */
    @GetMapping("/verProductos")
    @PreAuthorize("hasRole('Administrador')")
    public String verProductos(Model model, Authentication auth) {
        List<Producto> productos = productoService.obtenerTodo();

        model.addAttribute("productos", productos);
        model.addAttribute("usuario", (Usuario) auth.getPrincipal());
        model.addAttribute("titulo", "Todos los productos");

        return "pages/admin/verProductos";
    }

    /**
     * Muestra el formulario para agregar un nuevo producto.
     * Solo accesible para usuarios con el rol de 'Administrador'.
     *
     * @param modelo el modelo para pasar datos a la vista.
     * @param auth   la autenticación del usuario.
     * @return la vista para agregar un producto.
     */
    @GetMapping("/agregarProducto")
    @PreAuthorize("hasRole('Administrador')")
    public String verFormularioProducto(Model modelo, Authentication auth) {
        Map<Categoria, List<Categoria>> categorias = Utilidades.buscarCategorias(categoriaService.obtenerTodo());
        List<Almacen> almacenes = almacenService.obtenerTodo();

        modelo.addAttribute("categorias", categorias);
        modelo.addAttribute("almacenes", almacenes);
        modelo.addAttribute("usuario", (Usuario) auth.getPrincipal());
        modelo.addAttribute("titulo", "Agregar producto");

        return "pages/admin/nuevoProducto";
    }

    /**
     * Crea un nuevo producto con los parámetros proporcionados.
     * Solo accesible para usuarios con el rol de 'Administrador'.
     *
     * @param nombre        el nombre del producto.
     * @param descripcion   la descripción del producto.
     * @param precio        el precio del producto.
     * @param descuento     el descuento aplicado al producto.
     * @param peso          el peso del producto.
     * @param stock         la cantidad disponible en stock.
     * @param idCategoria   el id de la categoría del producto.
     * @param idAlmacen     el id del almacén del producto.
     * @param imagenes      las imágenes asociadas al producto.
     * @param auth          la autenticación del usuario.
     * @param redirectAttributes atributos para redirección y mensajes flash.
     * @return redirección a la página de productos o de error.
     */
    @PostMapping("/agregarProducto")
    @PreAuthorize("hasRole('Administrador')")
    public String agregarProducto(
            @RequestParam(name = "nombre") String nombre,
            @RequestParam(name = "descripcion") String descripcion,
            @RequestParam(name = "precio") float precio,
            @RequestParam(name = "descuento") float descuento,
            @RequestParam(name = "peso") float peso,
            @RequestParam(name = "stock") int stock,
            @RequestParam(name = "categoria") int idCategoria,
            @RequestParam(name = "almacen") int idAlmacen,
            @RequestParam(name = "imagenes", required = false) MultipartFile[] imagenes,
            Authentication auth, RedirectAttributes redirectAttributes) {
        try {
            // Validación de categoría
            Categoria categoria = categoriaService.buscarPorId(idCategoria).orElse(null);
            if (categoria == null) {
                throw new IllegalArgumentException("Error! La categoría ingresada es inválida.");
            }

            // Validación de almacén
            Almacen almacen = almacenService.buscarPorId(idAlmacen).orElse(null);
            if (almacen == null) {
                throw new IllegalArgumentException("Error! El almacen ingresado no existe.");
            }

            // Validación de descripción
            if (descripcion.length() > 500) {
                throw new IllegalArgumentException("Error! La descripción no puede tener más de 500 caracteres.");
            }

            // Validación de nombre
            if (nombre.length() > 50) {
                throw new IllegalArgumentException("Error! El nombre del producto no puede tener más de 50 caracteres.");
            }

            // Creación del producto
            Producto producto = productoService
                    .guardar(new Producto(nombre, descripcion, precio, descuento, peso, stock, categoria, almacen));

            // Manejo de imágenes
            if (imagenes != null) {
                for (MultipartFile imagen : imagenes) {
                    Imagen img = new Imagen(imagen.getBytes(), producto);
                    img = imagenService.guardar(img);
                    producto.getImagenes().add(img);
                }
            }

            // Modificación y actualización del producto
            producto = productoService.modificar(producto);

            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("mensaje", "Hecho! El producto se ha agregado correctamente.");

            return "redirect:/admin/verProductos";

        } catch (IllegalArgumentException | IOException e) {
            // Manejo de errores
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/admin/verProductos";
        }
    }
}
