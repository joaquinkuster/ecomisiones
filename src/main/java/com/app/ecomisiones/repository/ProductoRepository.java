package com.app.ecomisiones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ecomisiones.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
