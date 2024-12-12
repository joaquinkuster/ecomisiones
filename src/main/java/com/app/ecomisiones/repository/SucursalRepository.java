package com.app.ecomisiones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.ecomisiones.model.Sucursal;

/**
 * Repositorio para acceder y gestionar la entidad {@link Sucursal} en la base de datos.
 * Extiende de {@link JpaRepository}, proporcionando métodos CRUD básicos para la entidad {@link Sucursal}.
 */
@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {

    /**
     * Encuentra todas las sucursales que no están marcadas como inactivas (baja).
     * 
     * @return Una lista de {@link Sucursal} que no están inactivas.
     */
    List<Sucursal> findByBajaFalse();
}
