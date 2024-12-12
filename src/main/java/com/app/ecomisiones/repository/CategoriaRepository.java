package com.app.ecomisiones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.ecomisiones.model.Categoria;

/**
 * Repositorio para acceder y gestionar la entidad {@link Categoria} en la base de datos.
 * Extiende de {@link JpaRepository}, proporcionando métodos CRUD básicos para la entidad {@link Categoria}.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    /**
     * Busca las categorías que no están marcadas como inactivas.
     * Este método permite recuperar una lista de categorías que no están marcadas con la propiedad {@code baja = true}.
     * 
     * @return Una lista de categorías activas (no inactivas).
     */
    List<Categoria> findByBajaFalse();
}
