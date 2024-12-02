package com.app.ecomisiones.repository;

import com.app.ecomisiones.model.Compra;
import com.app.ecomisiones.model.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {

    List<Compra> findByBajaFalse();
    List<Compra> findByComprador(Usuario usuario);

}
