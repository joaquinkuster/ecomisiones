package com.app.ecomisiones.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class SucursalTest {

    @Test
    void testMarcarInactivo() {
        Ciudad ciudad = new Ciudad(); // Assuming Ciudad has a default constructor
        Sucursal sucursal = new Sucursal("123 Main St", ciudad);
        
        assertFalse(sucursal.esInactivo(), "Sucursal should be active initially");
        
        sucursal.marcarInactivo();
        
        assertTrue(sucursal.esInactivo(), "Sucursal should be inactive after calling marcarInactivo");
    }
}