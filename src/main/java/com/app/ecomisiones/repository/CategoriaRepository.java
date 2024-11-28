package com.app.ecomisiones.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.ecomisiones.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
