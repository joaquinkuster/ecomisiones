package com.app.ecomisiones.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class AlmacenTest {

    @Test
    void testConstructorAndGetters() {
        Ciudad ciudad = new Ciudad();
        Almacen almacen = new Almacen("123 Main St", ciudad);

        assertEquals("123 Main St", almacen.getDireccion());
        assertEquals(ciudad, almacen.getCiudad());
        assertFalse(almacen.getBaja());
    }

    @Test
    void testMarcarInactivo() {
        Almacen almacen = new Almacen();
        assertFalse(almacen.getBaja());

        almacen.marcarInactivo();
        assertTrue(almacen.getBaja());
    }

    @Test
    void testEsInactivo() {
        Almacen almacen = new Almacen();
        assertFalse(almacen.esInactivo());

        almacen.marcarInactivo();
        assertTrue(almacen.esInactivo());
    }

    @Test
    void testToString() {
        Ciudad ciudad = new Ciudad();
        ciudad.setNombre("Ciudad Test");

        Almacen almacen = new Almacen("123 Main St", ciudad);
        String expected = "123 Main St (Ciudad Test)";
        assertEquals(expected, almacen.toString());
    }
}