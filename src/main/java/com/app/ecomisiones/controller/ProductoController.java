package com.app.ecomisiones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.ecomisiones.config.SecurityConfig;
import com.app.ecomisiones.helper.Img;
import com.app.ecomisiones.model.*;
import com.app.ecomisiones.service.Almacen.AlmacenServiceImpl;
import com.app.ecomisiones.service.Categoria.CategoriaServiceImpl;
import com.app.ecomisiones.service.Imagen.ImagenServiceImpl;
import com.app.ecomisiones.service.Producto.ProductoServiceImpl;
import com.app.ecomisiones.service.Usuario.UsuarioServiceImpl;

import java.io.IOException;
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

    @Autowired
    private ImagenServiceImpl imagenService;

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private AlmacenServiceImpl almacenService;

    @Autowired
    private SecurityConfig securityConfig;

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

            List<Producto> relacionados = productoService.buscarPorCategoria(producto.getCategoria());

            // Convertir las imágenes a Base64
            List<String> imgPrincipales = Img.convertir(producto);

            Map<Producto, List<String>> relacionados2 = new HashMap<>();
            for (Producto relacionado : relacionados) {
                relacionados2.put(relacionado, Img.convertir(relacionado));
            }

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

    @GetMapping("/admin/productos")
    public String productos(Model modelo, Authentication auth) {
        List<Producto> productos = productoService.obtenerTodo();
        List<Categoria> categorias = categoriaService.obtenerTodo();
        List<Usuario> usuarios = usuarioService.obtenerTodo();
        List<Almacen> almacenes = almacenService.obtenerTodo();
        modelo.addAttribute("productos", productos);
        modelo.addAttribute("categorias", categorias);
        modelo.addAttribute("almacenes", almacenes);
        modelo.addAttribute("usuarios", usuarios);
        modelo.addAttribute("usuario", usuarios);
        return "/admin/productos";
    }

    @GetMapping("/crear")
    public String crear(Model model) {
        List<Categoria> categorias = categoriaService.obtenerTodo();
        List<Almacen> almacenes = almacenService.obtenerTodo();

        model.addAttribute("categorias", categorias);
        model.addAttribute("almacenes", almacenes);
        return "/productos/nuevoProducto";
    }

    @PostMapping("/crear")
    public String crear(
            @RequestParam(name = "nombre") String nombre,
            @RequestParam(name = "descripcion") String descripcion,
            @RequestParam(name = "precio") float precio,
            @RequestParam(name = "descuento") float descuento,
            @RequestParam(name = "peso") float peso,
            @RequestParam(name = "stock") int stock,
            @RequestParam(name = "vendedor", required = false) Integer idVendedor,
            @RequestParam(name = "categoria") int idCategoria,
            @RequestParam(name = "almacen") int idAlmacen,
            @RequestParam(name = "imagenes", required = false) MultipartFile[] imagenes) {
        try {

            System.out.println(idVendedor);

            Usuario vendedor = new Usuario();
            if (idVendedor == null) {
                vendedor = securityConfig.obtenerUsuarioAutenticado();
            } else {
                vendedor = usuarioService.buscarPorId(idVendedor).orElse(null);
                System.out.println(vendedor.toString());
            }

            Categoria categoria = categoriaService.buscarPorId(idCategoria).orElse(null);
            if (vendedor == null || categoria == null) {
                throw new IllegalArgumentException("Error al agregar producto: Alguno de los valores es nulo");
            }

            Almacen almacen = almacenService.buscarPorId(idAlmacen).orElse(null);
            if (almacen == null) {
                throw new IllegalArgumentException("Error! El almacen ingresado no existe.");

            }

            Producto producto = productoService
                    .guardar(new Producto(nombre, descripcion, precio, descuento, peso, stock, categoria, almacen));

            if (imagenes != null) {
                for (MultipartFile imagen : imagenes) {
                    Imagen img = new Imagen(imagen.getBytes(), producto);
                    img = imagenService.guardar(img);
                    producto.getImagenes().add(img);
                }
            }

            producto = productoService.modificar(producto);

            return "redirect:/inicio";

        } catch (IllegalArgumentException | IOException e) {
            System.out.println("Error al agregar producto: " + e);
            return "redirect:/admin/productos";
        }
    }
}
