package com.app.ecomisiones.controller;

import com.app.ecomisiones.model.Carrito;
import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Producto;

import com.app.ecomisiones.service.Carrito.CarritoService;
import com.app.ecomisiones.service.Producto.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private ProductoService productoService;

    // Método para mostrar el carrito de compras
    @GetMapping("/{usuarioId}")
    public String verCarrito(@PathVariable Long usuarioId, Model model) {
        Carrito carrito = carritoService.obtenerCarrito(usuarioId);
        model.addAttribute("carrito", carrito);
        return "carrito/verCarrito";
    }

    // Método para agregar un producto al carrito
    @PostMapping("/agregar/{carritoId}/{productoId}")
    public String agregarProductoAlCarrito(@PathVariable Long carritoId, @PathVariable Integer productoId) {
        carritoService.agregarProductoAlCarrito(carritoId, productoId);
        return "redirect:/carrito/" + carritoId;
    }

    // Método para eliminar un producto del carrito
    @PostMapping("/eliminar/{carritoId}/{productoId}")
    public String eliminarProductoDelCarrito(@PathVariable Long carritoId, @PathVariable Integer productoId) {
        carritoService.eliminarProductoDelCarrito(carritoId, productoId);
        return "redirect:/carrito/" + carritoId;
    }

    // Método para procesar la compra y generar la orden de compra
    @PostMapping("/procesar/{carritoId}")
    public String procesarCompra(@PathVariable Long carritoId) {
        carritoService.procesarCompra(carritoId);
        return "redirect:/carrito/" + carritoId;
    }

    // Método para filtrar productos por categoría
    @GetMapping("/productos/categoria/{categoriaId}")
    public String buscarProductosPorCategoria(@PathVariable Long categoriaId, Model model) {
        Categoria categoria = new Categoria(); // Aquí deberías obtener la categoría por su ID
        categoria.setId(categoriaId); // Asumiendo que la categoría tiene un método para establecer el ID
        List<Producto> productos = productoService.buscarPorCategoria(categoria);
        model.addAttribute("productos", productos);
        return "producto/listaProductos";
    }
}
