package com.app.ecomisiones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.app.ecomisiones.config.SecurityConfig;
import com.app.ecomisiones.model.*;
import com.app.ecomisiones.service.Categoria.CategoriaServiceImpl;
import com.app.ecomisiones.service.Imagen.ImagenServiceImpl;
import com.app.ecomisiones.service.Producto.ProductoServiceImpl;
import com.app.ecomisiones.service.Usuario.UsuarioServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Clase controlador para los productos en ecomarket.
 */

@Controller
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
    private SecurityConfig securityConfig;

    // Mostrar detalles del producto
    @GetMapping("productos/{id}")
    public String verProducto(@PathVariable int id, Model modelo, Authentication auth) {

        Usuario usuario = (Usuario) auth.getPrincipal();
        Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        List<Producto> productosRelacionados = productoService.buscarPorCategoria(producto.getCategoria());
        List<Categoria> categorias = categoriaService.obtenerTodo();

        // Convertir las imágenes a Base64
        List<String> imgPrincipales = producto.getImagenes()
                .stream()
                .map(imagen -> Base64.getEncoder().encodeToString(imagen.getImagen()))
                .toList();

        Map<Producto, List<String>> relacionadosConImagenes = new HashMap<>();
        for (Producto relacionado : productosRelacionados) {
            List<String> imgSecundarias = new ArrayList<>();
            for (Imagen imagen : relacionado.getImagenes()) {
                imgSecundarias.add(Base64.getEncoder().encodeToString(imagen.getImagen()));
            }
            relacionadosConImagenes.put(relacionado, imgSecundarias);
        }

        Set<DetalleCarrito> detalles = usuario.getCarrito().getDetalles();
        boolean estaEnCarrito = false;
        if (!detalles.isEmpty()) {
            for (DetalleCarrito detalle : detalles) {
                if (detalle.getProducto().getId() == producto.getId()){
                    estaEnCarrito = true;
                }
            }
        }

        modelo.addAttribute("producto", producto);
        modelo.addAttribute("estaEnCarrito", estaEnCarrito);
        modelo.addAttribute("imagenes", imgPrincipales);
        modelo.addAttribute("productosRelacionados", relacionadosConImagenes);
        modelo.addAttribute("categorias", categorias);
        modelo.addAttribute("usuario", usuario);
        return "vistaProducto";
    }

    @GetMapping("/admin/productos")
    public String productos(Model modelo, Authentication auth) {
        List<Producto> productos = productoService.obtenerTodo();
        List<Categoria> categorias = categoriaService.obtenerTodo();
        List<Usuario> usuarios = usuarioService.obtenerTodo();
        modelo.addAttribute("productos", productos);
        modelo.addAttribute("categorias", categorias);
        modelo.addAttribute("usuarios", usuarios);
        modelo.addAttribute("usuario", usuarios);
        return "/admin/productos";
    }

    @GetMapping("/productos/crear")
    public String crear(Model model) {
        List<Categoria> categorias = categoriaService.obtenerTodo();
        model.addAttribute("categorias", categorias);
        return "/productos/nuevoProducto";
    }

    @PostMapping("/productos/crear")
    public String crear(
            @RequestParam(name = "nombre") String nombre,
            @RequestParam(name = "descripcion") String descripcion,
            @RequestParam(name = "precio") float precio,
            @RequestParam(name = "descuento") float descuento,
            @RequestParam(name = "stock") int stock,
            @RequestParam(name = "vendedor", required = false) Integer idVendedor,
            @RequestParam(name = "categoria") int idCategoria,
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

            Producto producto = productoService
                    .guardar(new Producto(nombre, descripcion, precio, descuento, stock, vendedor,
                            categoria));

            if (imagenes != null) {
                for (MultipartFile imagen : imagenes) {
                    Imagen img = new Imagen(imagen.getBytes(), producto);
                    img = imagenService.guardar(img);
                    producto.getImagenes().add(img);
                }
            }

            producto = productoService.modificar(producto);

            return "redirect:/admin/productos";

        } catch (IllegalArgumentException | IOException e) {
            System.out.println("Error al agregar producto: " + e);
            return "redirect:/admin/productos";
        }
    }
}
