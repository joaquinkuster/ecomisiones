package com.app.ecomisiones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ecomisiones.model.Imagen;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Integer> {
}
