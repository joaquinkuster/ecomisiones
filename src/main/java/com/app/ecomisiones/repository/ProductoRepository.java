package com.app.ecomisiones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByCategoriaAndBajaFalse(Categoria categoria);
    List<Producto> findByBajaFalse();

    // Buscar todos los productos ordenados por fecha de creación descendente
    List<Producto> findAllByOrderByFechaCreacionDesc();

    // Buscar todos los productos ordenados por stock en orden ascendente
    List<Producto> findAllByOrderByStockAsc();

    // Consulta para obtener los productos ordenados por cantidad de ventas (de mayor a menor)
    @Query("SELECT p FROM Producto p ORDER BY SIZE(p.ventas) DESC")
    List<Producto> findProductosByVentasDesc();


}
