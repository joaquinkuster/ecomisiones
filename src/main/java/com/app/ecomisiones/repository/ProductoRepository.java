package com.app.ecomisiones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Producto;

/**
 * Repositorio para acceder y gestionar la entidad {@link Producto} en la base de datos.
 * Extiende de {@link JpaRepository}, proporcionando métodos CRUD básicos para la entidad {@link Producto}.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    /**
     * Encuentra todos los productos activos (no inactivos) que pertenecen a una categoría específica.
     * 
     * @param categoria La categoría a la que pertenecen los productos.
     * @return Una lista de {@link Producto} activos pertenecientes a la categoría.
     */
    List<Producto> findByCategoriaAndBajaFalse(Categoria categoria);

    /**
     * Encuentra todos los productos activos (no inactivos).
     * 
     * @return Una lista de {@link Producto} activos.
     */
    List<Producto> findByBajaFalse();

    /**
     * Encuentra todos los productos activos (no inactivos) ordenados por fecha de creación de forma descendente.
     * 
     * @return Una lista de {@link Producto} activos ordenados por fecha de creación descendente.
     */
    List<Producto> findByBajaFalseOrderByFechaCreacionDesc();

    /**
     * Encuentra todos los productos activos (no inactivos) ordenados por stock de forma ascendente.
     * 
     * @return Una lista de {@link Producto} activos ordenados por stock ascendente.
     */
    List<Producto> findByBajaFalseOrderByStockAsc();

    /**
     * Consulta personalizada que obtiene los productos activos (no inactivos) ordenados por la cantidad de ventas,
     * de mayor a menor.
     * 
     * @return Una lista de {@link Producto} activos ordenados por cantidad de ventas de mayor a menor.
     */
    @Query("SELECT p FROM Producto p WHERE p.baja = false ORDER BY SIZE(p.ventas) DESC")
    List<Producto> findByBajaFalseOrderByVentasDesc();
}
