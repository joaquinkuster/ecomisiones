package com.app.ecomisiones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.ecomisiones.model.Provincia;

/**
 * Repositorio para acceder y gestionar la entidad {@link Provincia} en la base de datos.
 * Extiende de {@link JpaRepository}, proporcionando métodos CRUD básicos para la entidad {@link Provincia}.
 */
@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, Integer> {

    /**
     * Encuentra todas las provincias que no están marcadas como inactivas (baja).
     * 
     * @return Una lista de {@link Provincia} que no están inactivas.
     */
    List<Provincia> findByBajaFalse();
}
