package com.app.ecomisiones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.ecomisiones.model.Ciudad;

/**
 * Repositorio para acceder y gestionar la entidad {@link Ciudad} en la base de datos.
 * Extiende de {@link JpaRepository}, proporcionando métodos CRUD básicos para la entidad {@link Ciudad}.
 */
@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Integer> {

    /**
     * Busca las ciudades que no están marcadas como inactivas.
     * Este método permite recuperar una lista de ciudades cuyo campo {@code baja} es {@code false}.
     * 
     * @return Una lista de ciudades activas (no inactivas).
     */
    List<Ciudad> findByBajaFalse();
}
