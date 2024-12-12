package com.app.ecomisiones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.ecomisiones.model.Imagen;

/**
 * Repositorio para acceder y gestionar la entidad {@link Imagen} en la base de datos.
 * Extiende de {@link JpaRepository}, proporcionando métodos CRUD básicos para la entidad {@link Imagen}.
 */
@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Integer> {
}
