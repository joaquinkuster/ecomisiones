package com.app.ecomisiones.model;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;





public class ProductoTest {

    private Producto producto;
    private Categoria categoria;
    private Almacen almacen;

    @BeforeEach
    public void setUp() {
        categoria = new Categoria();
        almacen = new Almacen();
        producto = new Producto("Producto1", "Descripcion1", 100.0f, 10.0f, 1.0f, 10, categoria, almacen);
    }

    @Test
    public void testGetPrecioConDescuento() {
        float precioConDescuento = producto.getPrecioConDescuento();
        assertEquals(90.0f, precioConDescuento, 0.01);
    }

    @Test
    public void testMarcarInactivo() {
        producto.marcarInactivo();
        assertTrue(producto.esInactivo());
    }

    @Test
    public void testEsInactivo() {
        assertFalse(producto.esInactivo());
        producto.marcarInactivo();
        assertTrue(producto.esInactivo());
    }

    @Test
    public void testToString() {
        assertEquals("Producto1", producto.toString());
    }

    @Test
    public void testConstructor() {
        assertEquals("Producto1", producto.getNombre());
        assertEquals("Descripcion1", producto.getDescripcion());
        assertEquals(100.0f, producto.getPrecio());
        assertEquals(10.0f, producto.getDescuento());
        assertEquals(1.0f, producto.getPeso());
        assertEquals(10, producto.getStock());
        assertEquals(categoria, producto.getCategoria());
        assertEquals(almacen, producto.getAlmacen());
        assertEquals(LocalDate.now(), producto.getFechaCreacion());
        assertFalse(producto.getBaja());
    }
}