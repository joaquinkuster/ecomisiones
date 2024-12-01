package com.app.ecomisiones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ecomisiones.model.Ciudad;
@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Integer> {
    List<Ciudad> findByBajaFalse();
}
