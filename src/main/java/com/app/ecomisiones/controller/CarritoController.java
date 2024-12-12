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

/**
 * Controlador para gestionar las operaciones relacionadas con el carrito de compras.
 * 
 * Proporciona funcionalidades para ver, agregar y eliminar productos en el carrito.
 */
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

    /**
     * Método para mostrar el carrito de compras.
     *
     * @param model               El modelo que se utiliza para pasar datos a la vista.
     * @param auth                La autenticación del usuario.
     * @param redirectAttributes Los atributos para redirigir mensajes.
     * @return El nombre de la vista para el carrito.
     */
    @GetMapping("/ver")
    public String verCarrito(Model model, Authentication auth, RedirectAttributes redirectAttributes) {
        try {
            // Obtiene el usuario autenticado y las categorías
            Usuario usuario = (Usuario) auth.getPrincipal();
            List<Categoria> categorias = categoriaService.obtenerTodo();

            // Verifica que el usuario exista
            if (usuario == null) {
                throw new IllegalArgumentException("Error! El usuario ingresado no existe.");
            }

            // Calcula el total del carrito y lo redondea a 2 decimales
            double total = usuario.getCarrito().calcularTotal();

            // Agrega los datos al modelo para ser utilizados en la vista
            model.addAttribute("usuario", usuario);
            model.addAttribute("categorias", categorias);
            model.addAttribute("carrito", usuario.getCarrito());
            model.addAttribute("total", Math.round(total * 100.0) / 100.0);
            model.addAttribute("titulo", "Carrito de compras");

            return "pages/carrito";

        } catch (Exception e) {
            // Manejo de errores, redirige con el mensaje de error
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";
        }
    }

    /**
     * Método para agregar un producto al carrito.
     *
     * @param id                  El ID del producto a agregar.
     * @param cantidad            La cantidad del producto a agregar.
     * @param auth                La autenticación del usuario.
     * @param redirectAttributes Los atributos para redirigir mensajes.
     * @return La vista redirigida después de agregar el producto.
     */
    @PostMapping("/agregar/{id}")
    public String agregarProductoAlCarrito(@PathVariable int id,
                          @RequestParam(name = "cantidad", required = false) int cantidad,
                          Authentication auth,
                          RedirectAttributes redirectAttributes) {

        try {
            // Obtiene el usuario autenticado y el producto correspondiente
            Usuario usuario = (Usuario) auth.getPrincipal();
            Producto producto = productoService.buscarPorId(id).orElse(null);

            // Verifica que el usuario y el producto sean válidos
            if (usuario == null || producto == null) {
                throw new IllegalArgumentException("Error! El usuario o el producto ingresado no existe.");
            }

            // Verifica que la cantidad ingresada sea válida
            if (cantidad <= 0) {
                throw new IllegalArgumentException("Error! La cantidad ingresada es inválida.");
            }

            // Verifica que haya suficiente stock disponible
            if (cantidad > producto.getStock()) {
                throw new IllegalArgumentException("Error! No hay suficiente stock disponible.");
            }

            // Agrega el producto al carrito
            DetalleCarrito detalle = new DetalleCarrito(cantidad, producto, usuario.getCarrito());
            detalleCarritoService.guardar(detalle);
            usuario.getCarrito().agregarDetalle(detalle);

            // Actualiza el stock del producto
            producto.setStock(producto.getStock() - cantidad);
            productoService.modificar(producto);

            // Redirige con un mensaje de éxito
            redirectAttributes.addFlashAttribute("mensaje", "Hecho! El producto se ha agregado al carrito correctamente.");

            // Calcula el nuevo total y guarda el carrito actualizado
            // usuario.getCarrito().calcularTotal();
            carritoService.modificar(usuario.getCarrito());

            // Redirige al detalle del producto
            return "redirect:/producto/ver/" + id;

        } catch (Exception e) {
            // Manejo de errores, redirige con el mensaje de error
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";
        }
    }

    /**
     * Método para eliminar un producto del carrito.
     *
     * @param id                  El ID del producto a eliminar.
     * @param vista               La vista a la que redirigir después de la eliminación.
     * @param auth                La autenticación del usuario.
     * @param redirectAttributes Los atributos para redirigir mensajes.
     * @return La vista redirigida después de eliminar el producto.
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarProductoDelCarrito(@PathVariable int id,
                           @RequestParam(name = "vista", required = false) String vista,
                           Authentication auth,
                           RedirectAttributes redirectAttributes) {

        try {
            // Obtiene el usuario autenticado y el producto correspondiente
            Usuario usuario = (Usuario) auth.getPrincipal();
            Producto producto = productoService.buscarPorId(id).orElse(null);

            // Verifica que el usuario y el producto sean válidos
            if (usuario == null || producto == null) {
                throw new IllegalArgumentException("Error! El usuario o el producto ingresado no existe.");
            }

            // Obtiene los detalles del carrito del usuario
            Set<DetalleCarrito> detalles = usuario.getCarrito().getDetalles();

            // Busca el detalle del producto en el carrito y lo elimina
            for (DetalleCarrito detalle : detalles) {
                if (detalle.getProducto().getId() == producto.getId()) {
                    // Marca el detalle como inactivo y actualiza el stock del producto
                    detalle.marcarInactivo();
                    producto.setStock(producto.getStock() + detalle.getCantidad());
                    productoService.modificar(producto);
                    detalleCarritoService.borrar(detalle);
                    redirectAttributes.addFlashAttribute("mensaje", "Hecho! El producto se ha eliminado del carrito correctamente.");
                    break;
                }
            }

            // Calcula el nuevo total y guarda el carrito actualizado
            // usuario.getCarrito().calcularTotal();
            carritoService.modificar(usuario.getCarrito());

            // Redirige a la vista correspondiente, si es "carrito", muestra el carrito
            return vista == null || !vista.equalsIgnoreCase("carrito")
                    ? "redirect:/producto/ver/" + id
                    : "redirect:/carrito/ver";

        } catch (Exception e) {
            // Manejo de errores, redirige con el mensaje de error
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";
        }
    }
}
