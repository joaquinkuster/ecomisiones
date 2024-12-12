package com.app.ecomisiones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ecomisiones.model.Almacen;

/**
 * Repositorio para acceder y gestionar la entidad {@link Almacen} en la base de datos.
 * Extiende de {@link JpaRepository}, lo que proporciona métodos CRUD básicos y consultas adicionales.
 */
@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {

    /**
     * Recupera una lista de almacenes que no están marcados como inactivos (baja = false).
     * Este método permite obtener solo los almacenes activos, excluyendo aquellos que han sido deshabilitados.
     * 
     * @return Una lista de almacenes activos (con baja = false).
     */
    List<Almacen> findByBajaFalse();
}
