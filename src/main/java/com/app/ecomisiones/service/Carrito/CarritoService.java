package com.app.ecomisiones.service.Carrito;

import com.app.ecomisiones.model.Carrito;
import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Producto;
import com.app.ecomisiones.repository.CarritoRepository;
import com.app.ecomisiones.repository.ProductoRepository;
import com.app.ecomisiones.model.OrdenDeCompra;
import com.app.ecomisiones.repository.OrdenDeCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private OrdenDeCompraRepository ordenDeCompraRepository;

    // Obtener carrito por ID de usuario
    public Carrito obtenerCarrito(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId).orElseThrow(() -> new NoSuchElementException("Carrito no encontrado"));
    }

    // Agregar producto al carrito
    public void agregarProductoAlCarrito(Long carritoId, Integer productoId) {
        Carrito carrito = carritoRepository.findById(carritoId).orElseThrow(() -> new NoSuchElementException("Carrito no encontrado"));
        Producto producto = productoRepository.findById(productoId).orElseThrow(() -> new NoSuchElementException("Producto no encontrado"));
        carrito.getProductos().add(producto);
        carrito.setTotal(carrito.getTotal() + producto.getPrecio());
        carritoRepository.save(carrito);
    }

    // Eliminar producto del carrito
    public void eliminarProductoDelCarrito(Long carritoId, Integer productoId) {
        Carrito carrito = carritoRepository.findById(carritoId).orElseThrow(() -> new NoSuchElementException("Carrito no encontrado"));
        Producto producto = productoRepository.findById(productoId).orElseThrow(() -> new NoSuchElementException("Producto no encontrado"));
        carrito.getProductos().remove(producto);
        carrito.setTotal(carrito.getTotal() - producto.getPrecio());
        carritoRepository.save(carrito);
    }

    // Procesar compra y generar orden de compra
    public void procesarCompra(Long carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId).orElseThrow(() -> new NoSuchElementException("Carrito no encontrado"));
        // Generar orden de compra
        OrdenDeCompra ordenDeCompra = new OrdenDeCompra();
        ordenDeCompra.setUsuario(carrito.getUsuario());
        ordenDeCompra.setProductos(carrito.getProductos());
        ordenDeCompra.setTotal(carrito.getTotal());
        ordenDeCompra.setEstado("Pendiente");
        // Aquí se guarda la orden de compra
        ordenDeCompraRepository.save(ordenDeCompra);

        // Vaciar carrito
        carrito.getProductos().clear();
        carrito.setTotal(0);
        carritoRepository.save(carrito);
    }

    // Método adicional para filtrar productos por categoría
    public List<Producto> obtenerProductosPorCategoria(Categoria categoria) {
        return productoRepository.findByCategoria(categoria);
    }
}
