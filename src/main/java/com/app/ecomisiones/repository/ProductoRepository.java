package com.app.ecomisiones.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.ecomisiones.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
