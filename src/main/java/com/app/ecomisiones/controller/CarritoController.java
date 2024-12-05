package com.app.ecomisiones.controller;

import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.DetalleCarrito;
import com.app.ecomisiones.model.Producto;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.service.Carrito.CarritoServiceImpl;
import com.app.ecomisiones.service.Categoria.CategoriaServiceImpl;
import com.app.ecomisiones.service.DetalleCarrito.DetalleCarritoServiceImpl;
import com.app.ecomisiones.service.Producto.ProductoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Set;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private DetalleCarritoServiceImpl detalleCarritoService;

    @Autowired
    private ProductoServiceImpl productoService;

    @Autowired
    private CategoriaServiceImpl categoriaService;

    @Autowired
    private CarritoServiceImpl carritoService;

    // Método para mostrar el carrito de compras
    @GetMapping("/ver")
    public String ver(Model model, Authentication auth, RedirectAttributes redirectAttributes) {
        try {

            Usuario usuario = (Usuario) auth.getPrincipal();
            List<Categoria> categorias = categoriaService.obtenerTodo();

            if (usuario == null) {
                throw new IllegalArgumentException("Error! El usuario ingresado no existe.");
            }

            model.addAttribute("usuario", usuario);
            model.addAttribute("categorias", categorias);
            model.addAttribute("carrito", usuario.getCarrito());
            model.addAttribute("total", usuario.getCarrito().calcularTotal());

            return "carrito";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";

        }
    }

    @PostMapping("/agregar/{id}")
    public String agregar(@PathVariable int id,
            @RequestParam(name = "cantidad", required = false) int cantidad,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        try {
            Usuario usuario = (Usuario) auth.getPrincipal();
            Producto producto = productoService.buscarPorId(id).orElse(null);

            if (usuario == null || producto == null) {
                throw new IllegalArgumentException("Error! El usuario o el producto ingresado no existe.");
            }

            if (cantidad <= 0) {
                throw new IllegalArgumentException("Error! La cantidad ingresada es inválida.");
            }

            if (cantidad > producto.getStock()) {
                throw new IllegalArgumentException("Error! No hay suficiente stock disponible.");
            }

            // Agregar producto al carrito
            DetalleCarrito detalle = new DetalleCarrito(cantidad, producto, usuario.getCarrito());
            detalleCarritoService.guardar(detalle);
            usuario.getCarrito().agregarDetalle(detalle);

            // Actualizar stock del producto
            producto.setStock(producto.getStock() - cantidad);
            productoService.modificar(producto);

            redirectAttributes.addFlashAttribute("mensaje", "Hecho! Producto agregado al carrito correctamente.");

            usuario.getCarrito().calcularTotal();
            carritoService.modificar(usuario.getCarrito());
            // usuario.setCarrito(carritoService.buscarPorUsuario(usuario));

            return "redirect:/producto/ver/" + id;

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";
        
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id,
            @RequestParam(name = "vista", required = false) String vista,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        try {
            Usuario usuario = (Usuario) auth.getPrincipal();
            Producto producto = productoService.buscarPorId(id).orElse(null);

            if (usuario == null || producto == null) {
                throw new IllegalArgumentException("Error! El usuario o el producto ingresado no existe.");
            }

            Set<DetalleCarrito> detalles = usuario.getCarrito().getDetalles();

            for (DetalleCarrito detalle : detalles) {
                if (detalle.getProducto().getId() == producto.getId()) {
                    // Si el producto ya está en el carrito, lo eliminamos
                    detalle.marcarInactivo();
                    producto.setStock(producto.getStock() + detalle.getCantidad());
                    productoService.modificar(producto);
                    detalleCarritoService.borrar(detalle);
                    redirectAttributes.addFlashAttribute("mensaje", "Hecho! Producto eliminado del carrito correctamente.");
                    break;
                }
            }

            usuario.getCarrito().calcularTotal();
            carritoService.modificar(usuario.getCarrito());
            // usuario.setCarrito(carritoService.buscarPorUsuario(usuario));

            return vista == null || !vista.equalsIgnoreCase("carrito")
                    ? "redirect:/producto/ver/" + id
                    : "redirect:/carrito/ver";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";
        
        }
    }

}
