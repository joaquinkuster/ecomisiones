package com.app.ecomisiones.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;



public class CarritoTest {

    private Carrito carrito;
    private Usuario usuario;
    private DetalleCarrito detalle1;
    private DetalleCarrito detalle2;
    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario();
        carrito = new Carrito(usuario);

        detalle1 = new DetalleCarrito();
        detalle1.setProducto(producto1);
        detalle1.setCantidad(2);

        detalle2 = new DetalleCarrito();
        detalle2.setProducto(producto2);
        detalle2.setCantidad(1);

        carrito.agregarDetalle(detalle1);
        carrito.agregarDetalle(detalle2);
    }

    @Test
    public void testAgregarDetalle() {
        assertEquals(2, carrito.getDetalles().size());
    }

    @Test
    public void testCalcularTotal() {
        double total = carrito.calcularTotal();
        assertEquals(400.0, total);
    }

    @Test
    public void testMarcarInactivo() {
        carrito.marcarInactivo();
        assertTrue(carrito.esInactivo());
    }

    @Test
    public void testGetDetalles() {
        Set<DetalleCarrito> detalles = carrito.getDetalles();
        assertEquals(2, detalles.size());
    }

    @Test
    public void testGetDetallesExcludesInactive() {
        detalle1.marcarInactivo();
        Set<DetalleCarrito> detalles = carrito.getDetalles();
        assertEquals(1, detalles.size());
    }
}