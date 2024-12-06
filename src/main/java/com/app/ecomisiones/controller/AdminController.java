package com.app.ecomisiones.controller;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("")
    @PreAuthorize("hasRole('Administrador')")
    public String admin(Model model, Authentication auth) {
        model.addAttribute("usuario", (Usuario) auth.getPrincipal());
        return "admin/admin";
    }

    // Método para ver mis pedidos
    @GetMapping("/verPedidos")
    @PreAuthorize("hasRole('Administrador')")
    public String ver(Model model, Authentication auth) {
        model.addAttribute("usuario", (Usuario) auth.getPrincipal());
        model.addAttribute("pedidos", pedidoService.obtenerTodo());

        return "admin/verPedidos";

    }

    @GetMapping("/verProductos")
    @PreAuthorize("hasRole('Administrador')")
    public String verProductos(Model model, Authentication auth) {
        List<Producto> productos = productoService.obtenerTodo();

        model.addAttribute("productos", productos);
        model.addAttribute("usuario", (Usuario) auth.getPrincipal());
        return "admin/verProductos";
    }

    @GetMapping("/agregarProducto")
    @PreAuthorize("hasRole('Administrador')")
    public String agregarProducto(Model modelo, Authentication auth) {
        List<Categoria> categorias = categoriaService.obtenerTodo();
        List<Almacen> almacenes = almacenService.obtenerTodo();

        modelo.addAttribute("categorias", categorias);
        modelo.addAttribute("almacenes", almacenes);
        modelo.addAttribute("usuario", (Usuario) auth.getPrincipal());
        return "admin/nuevoProducto";
    }

    @PostMapping("/agregarProducto")
    @PreAuthorize("hasRole('Administrador')")
    public String crear(
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

            Categoria categoria = categoriaService.buscarPorId(idCategoria).orElse(null);
            if (categoria == null) {
                throw new IllegalArgumentException("Error! La categoría ingresada es inválida.");
            }

            Almacen almacen = almacenService.buscarPorId(idAlmacen).orElse(null);
            if (almacen == null) {
                throw new IllegalArgumentException("Error! El almacen ingresado no existe.");

            }

            if (descripcion.length() > 500) {
                throw new IllegalArgumentException("Error! La descripción no puede tener más de 500 caracteres.");
            }

            //haceme un limite de caracteres para el nombre del producto en 50
            if (nombre.length() > 50) {
                throw new IllegalArgumentException("Error! El nombre del producto no puede tener más de 50 caracteres.");
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

            redirectAttributes.addFlashAttribute("mensaje", "Hecho! El producto se ha agregado correctamente.");

            return "redirect:/admin/verProductos";

        } catch (IllegalArgumentException | IOException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/admin/verProductos";
        }
    }
}
