package com.app.ecomisiones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.ecomisiones.model.DetalleCarrito;

/**
 * Repositorio para acceder y gestionar la entidad {@link DetalleCarrito} en la base de datos.
 * Extiende de {@link JpaRepository}, proporcionando métodos CRUD básicos para la entidad {@link DetalleCarrito}.
 */
@Repository
public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Integer> {

    /**
     * Busca los detalles de carrito que no están marcados como inactivos.
     * Este método permite recuperar una lista de detalles de carrito cuyo campo {@code baja} es {@code false}.
     * 
     * @return Una lista de detalles de carrito activos (no inactivos).
     */
    List<DetalleCarrito> findByBajaFalse();
}
